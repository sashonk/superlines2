package superlines.core;

import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Configuration {
	
	private final static Log log = LogFactory.getLog(Configuration.class); 
	private static Configuration instance;
	private Properties m_props;
	private File m_dataFolder;
	
	public static Configuration get(){
		if(instance == null){
			try{
				instance = new Configuration();
			}
			catch(Exception  ex){
				log.error("failed create configuration:", ex);
			}
		}
		
		return instance;
	}
	
	private Configuration() throws Exception{
		m_props = new Properties();
		
		String configFilePath = System.getProperty("config.file.path");
		FileInputStream ifstr = new FileInputStream(new File(configFilePath));
		
		m_props.load(ifstr);
		
		ifstr.close();
		
	}
	
	public String getProperty(String key){
		return m_props.getProperty(key);
	}
	
	public Properties getConfig(){
		return m_props;
	}
	
	public File getDataFolder(){
		if(m_dataFolder==null){
			String prop = System.getProperty("data.folder");
			if(prop!=null){
				m_dataFolder = new File(prop);
			}
		}
		
		return m_dataFolder;
	}

}
