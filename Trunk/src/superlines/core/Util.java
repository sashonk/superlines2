package superlines.core;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;



public class Util {
	
	private static final Pattern EMAIL_ADDRESS_PATTERN =
			Pattern
			.compile("([0-9A-Za-z-_]+\\.)*[0-9A-Za-z-_]+@((([0-9A-Za-z-_]+\\.)+[A-Za-z]+)|(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}))"); 
	
	public static String toString(final Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		 e.printStackTrace(pw);
		 return sw.toString();
	}
	
	
	public static boolean isBlank(final String str){
		return str == null || str.equals("");
	}
	
	public static boolean isValidEmailAddress(final String str) {
	    if (isBlank(str)) {
	        return false;
	    }
	    return EMAIL_ADDRESS_PATTERN.matcher(str).matches();
	}
	
	

	
	public static String getRelativePath(final File root, final File file){
		if(root.equals(file)){
			return "";
		}
		
		StringBuilder path = new StringBuilder();
		List<String> names = new LinkedList<>();
		File parent = file.getParentFile();
		names.add(file.getName());
		while(!parent.equals(root)){
			names.add(parent.getName());
			parent = parent.getParentFile();
		}
		
		for(int i = names.size()-1; i>=0; i--){
			path.append(names.get(i));
			if(i>0){
				path.append("/");
			}
		}
		
		return path.toString();
		
	}


	// This method writes a DOM document to a file
	public static void writeXmlFile(Document doc, File file) {
	    try {
	    	
	        //Prepare the DOM document for writing
	        Source source = new DOMSource(doc);
	
	        // Prepare the output file
	       // File file = new File(filename);
	        Result result = new StreamResult(file);
	
	        // Write the DOM document to the file
	        Transformer xformer = TransformerFactory.newInstance().newTransformer();
	        xformer.transform(source, result);
	    } catch (TransformerConfigurationException e) {
	    } catch (TransformerException e) {
	    }
	}



}

