package superlines.client.online;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import superlines.client.FeedBack;
import superlines.client.Messages;
import superlines.client.ws.ServiceAdapter;

import superlines.core.Util;

public class Updater {
	
	private  String[] updatedDirsNames = new String[] {"classes", "data", "config"};
	
	private Updater() throws Exception{
	
		ServiceAdapter sa = ServiceAdapter.get();
		Class.forName("superlines.core.Util");
		

		for(int i = 0; i<updatedDirsNames.length;i++){
			String updatedDirName = updatedDirsNames[i];
			byte[] data  = sa.getChecksumDocument(updatedDirName);
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			m_chsumDocuments.put(updatedDirName, (Document) ois.readObject());
			
			
			File checkSumFile = new File("update", String.format("%s.xml", updatedDirName));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();				
			m_ownChsumDocuments.put(updatedDirName, db.parse(checkSumFile));															

		}
	}
	
	private static final Log log = LogFactory.getLog(Updater.class);
	
	private Map<String, Document> m_chsumDocuments = new HashMap<>();
	private Map<String, Document> m_ownChsumDocuments = new HashMap<>(); 
	
	private static Updater instance;
	
	public static Updater get(){
		if(instance==null){
			try{
				instance = new Updater();
			}
			catch(Exception ex){
				log.error(ex);
			}
		}
		
		return instance;
	}
	
	public  boolean updatesAvailable() throws Exception{
		for(String updatedDirName : updatedDirsNames){
			Document ownDoc = m_ownChsumDocuments.get(updatedDirName);
			Document doc = m_chsumDocuments.get(updatedDirName);
			
			String ownChsum = ownDoc.getDocumentElement().getAttribute("chsum");
			String chsum = doc.getDocumentElement().getAttribute("chsum");
									
			if(!ownChsum.equals(chsum)){
				return true;
			}
		}
		
		return false;
	}
	
	private void updateChsumDocuments(){
	
		for(String updateDirName : m_chsumDocuments.keySet()){
			File chsumFile = new File("update", updateDirName+".xml");
			chsumFile.delete();
			
			Util.writeXmlFile(m_chsumDocuments.get(updateDirName), chsumFile);
			
		}
	}
	
	private int  totalCount(final Map<String, Set<String>> map){
		int total = 0;
		for(String key : map.keySet()){
			Set<String> set = map.get(key);
			total+=set.size();
		}
		
		return total;
	}
	
	public  void update(final FeedBack feedback) throws Exception{				
	
		feedback.begin();
		
		for(String updatedDirName : updatedDirsNames){						
			File updatedDir = new File(updatedDirName);
			
			 
			Map<String, Set<String>> filesToUpdateMap = getFilesToUpdate(updatedDirName);
			
			
			int total = totalCount(filesToUpdateMap);
			int count = 0;
			for(String path : filesToUpdateMap.keySet()){
				Set<String> filesToUpdate = filesToUpdateMap.get(path);
				
				for(String outdatedFileName : filesToUpdate){	
					
					//Thread.sleep(1000);
					
					File fileToUpdateDir = new File(updatedDir, path);
					fileToUpdateDir.mkdirs();
					File fileToUpdate = new File(fileToUpdateDir,outdatedFileName );
					
					//fileToUpdate.delete();
					
					String fileName = String.format("%s/%s",updatedDir , Util.getRelativePath(updatedDir, fileToUpdate));
					feedback.updateTitle(String.format("updating file: %s", fileName));
					byte[] b = ServiceAdapter.get().getFile(fileName);
					ByteArrayInputStream bais = new ByteArrayInputStream(b);
					
					GZIPInputStream ogzip = null;
					FileOutputStream ofstr = null;
					try{
					 ogzip = new GZIPInputStream(bais);
					 ofstr = new FileOutputStream(fileToUpdate);
					
					byte[] decompressed = new byte[1024];
					int read = 0;
					
					while((read = ogzip.read(decompressed)) > 0){
						ofstr.write(decompressed,0, read);
					}

					
					}
					catch(Exception ex){
						log.error(ex);					
					}
					finally{
						if(ogzip!=null){
							ogzip.close();
						}
						if(ofstr!=null){
							ofstr.close();
						}
					}
					
					
					count++;
					float progress = count / (float)total;
					feedback.updateProgress(progress);
					
				}
			}
			

		}
		
	
			updateChsumDocuments();
			feedback.updateTitle(Messages.RELAUNCH.toString());
			
			
		
		feedback.end();
	}
	
	private  Map<String, Set<String>> getFilesToUpdate(final String dirName){
		Map<String, Set<String>> result = new HashMap<>();
		
		Document own = m_ownChsumDocuments.get(dirName);
			
		
		Document doc = null;
		
		try{

			doc = m_chsumDocuments.get(dirName);
			Element root = doc.getDocumentElement();
			
			NodeList nodeList =  root.getChildNodes();
			Set<File> dirsToDelete = new HashSet<>();
			for(int i = 0; i<nodeList.getLength(); i++){
				Node node = nodeList.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE){
					Element directoryEl = (Element)node;
					
					String path  = directoryEl.getAttribute("path");
					String chsum = directoryEl.getAttribute("chsum");

					Element ownDirectory = findAndRemove(path, own);
					if(ownDirectory==null || !ownDirectory.getAttribute("chsum").equals(chsum)){
						
						
						dirsToDelete.add(new File(dirName, path));
						Set<String> set = ServiceAdapter.get().listFiles(String.format("%s/%s", dirName, path));
						result.put(path, set);
						
					}
				}
			}
			
			for(File f : dirsToDelete){
				emptyDir(f);
			}
			deleteTheRest(own, dirName);
		}
		catch(Exception ex){
			log.error(ex);
		}
	
		return result;
	}
	
	private void emptyDir(final File dir){
		File[] files = dir.listFiles();
		
		if(files!=null){
			for(File f : files){
				if(f.isFile()){
					f.delete();
				}
			}
		}

	}
	
	private  void deleteDirectoryRecursive(final File dir){
		File[] files = dir.listFiles();
		
		if(files!=null){
			for(File f : files){
				if(f.isFile()){
					f.delete();
				}
				else if(f.isDirectory()){
					deleteDirectoryRecursive(f);
				}
			}
		}
		
		dir.delete();
	}
	
	private  void deleteTheRest(final Document doc, final String rootDirName){
		Element root = doc.getDocumentElement();
		
		NodeList nl = root.getChildNodes();
		for(int i = 0; i<nl.getLength(); i++){
			Node nd = nl.item(i);
			if(nd.getNodeType()==Node.ELEMENT_NODE){
				Element directory = (Element)nd;
				String path = directory.getAttribute("path");
				File dir = new File(rootDirName, path);
				deleteDirectoryRecursive(dir);
			}
		}
	}
	
	private  Element findAndRemove(final String pathValue,  final Document doc){
		Element root = doc.getDocumentElement();
		NodeList nl = root.getChildNodes();
		for(int i = 0; i<nl.getLength(); i++){
			Node n = nl.item(i);
			if(n.getNodeType()==Node.ELEMENT_NODE){
				Element entry = (Element)n;
				if(entry.getAttribute("path").equals(pathValue)){
					entry.getParentNode().removeChild(entry);
					return entry;
				}
			}
		}
		
		return null;
	}
	

}
