package superlines.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class CheckSumGenerator {
	
	private static CheckSumGenerator instance;
	private static final int SIZE = 4096*2;
	byte[] m_data = new byte[SIZE];
	
	public static CheckSumGenerator get(){
		if(instance==null){
			try{							
				instance = new CheckSumGenerator();
			}
			catch(Exception ex){
				System.err.println(ex);
			}
		}
		
		return instance;
	}
	
	public static void main(String[] ar) throws Exception{
		
		if(ar.length==0){
			System.out.println("not enough arguments");
			return;
		}
		
		String dirName = ar[0];
		String[] ignoredPatterns = new String[ar.length-1];
		for(int i = 0; i<ar.length-1 ;i++){
			ignoredPatterns[i] = ar[i+1];
		}
					
		Document doc =CheckSumGenerator.get().generate(new File(dirName), ignoredPatterns);
		

		Util.writeXmlFile(doc, new File(dirName+".xml"));
		

	}
	
	private CheckSumGenerator() throws Exception{
	}
	

	
	
	public Document generate(final File parentDir, final String... ignoredPatterns) throws Exception{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();		
		Document doc = db.newDocument();
		Element root = doc.createElement("root");
		root.setAttribute("path", parentDir.getName());
		doc.appendChild(root);
		
		MessageDigest rootDigest = MessageDigest.getInstance("MD5");
		processDir(parentDir, parentDir, root, doc, rootDigest,  ignoredPatterns);
		root.setAttribute("chsum",  digestToString(rootDigest.digest()));
		
		return doc;
	}
	
	private  void processDir(final File dir, final File rootDir, final Element element, final Document doc,  final MessageDigest rootDigest, final String... ignoredPatterns) throws Exception{		
			Element root = doc.getDocumentElement();
		File[] files = dir.listFiles();		
		MessageDigest digest = MessageDigest.getInstance("MD5");
	
		for(File file : files){
			if(file.isDirectory()){
				
				String path = Util.getRelativePath(rootDir, file);
				
				boolean contains = false;
				for(String pat : ignoredPatterns){
					if(path.contains(pat)){
						contains = true;
					}
				}
				if(contains){				

				}
				else{
					Element child = doc.createElement("directory");
					child.setAttribute("path", path);
					root.appendChild(child);				
					processDir(file, rootDir,  child, doc,rootDigest , ignoredPatterns );			
				}
			}
			else if(file.isFile()){
				
				FileInputStream ifstr = new FileInputStream(file);
				int read = 0;
				
				do{
					Arrays.fill(m_data, (byte)0);
					read = ifstr.read(m_data, 0, SIZE);
					digest.update(m_data);	
					rootDigest.update(m_data);
				}while(read==SIZE);
				
									
				ifstr.close();
			}
			
		}
		
		element.setAttribute("chsum", digestToString(digest.digest()));
	}
	
	private static void print(Document doc){
		System.out.println(toString(doc));
	}
	
	private static String digestToString(final byte[] digest){
		BigInteger value = new BigInteger(1, digest);
		return value.toString(16);
	}
	
    private static String toString(Document doc) {
	    try {
	        StringWriter sw = new StringWriter();
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        transformer.transform(new DOMSource(doc), new StreamResult(sw));
	        return sw.toString();
	    } catch (Exception ex) {
	        throw new RuntimeException("Error converting to String", ex);
	    }
	}
}
