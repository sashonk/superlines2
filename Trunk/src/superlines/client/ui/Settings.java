package superlines.client.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Settings {
	
	private File settingsFolder = new File("settings/");
	private Properties m_props = new Properties();
	private final String m_key;
	
	public Settings(final String key) throws Exception{
		m_key = key;
		if(!settingsFolder.exists()){
			settingsFolder.mkdir();
		}
		
		File settingsFile = new File(settingsFolder, key);
		if(settingsFile.exists()){
			
			FileInputStream ifstr = new FileInputStream(settingsFile);
			m_props.load(ifstr);
			ifstr.close();
		}
		else{
			m_props.setProperty("x", "0");
			m_props.setProperty("y", "0");
			m_props.setProperty("width", "0");
			m_props.setProperty("height", "0");
		}

				
	}
	
	
	public void flush() throws Exception{
		File settingsFile = new File(settingsFolder, m_key);
		if(!settingsFile.exists()){
			settingsFile.createNewFile();
		}
		
		FileOutputStream ofstr = new FileOutputStream(settingsFile);
		m_props.store(ofstr, "");		
		ofstr.close();
	}

	public int getWidth(){
		return Integer.valueOf(m_props.getProperty("width"));
	}
	
	public void setWidth(final int width){
		m_props.setProperty("width", Integer.toString(width));
	}
	
	public int getHeight(){
		return Integer.valueOf(m_props.getProperty("height"));
	}
	
	public void setHeight(final int height){
		m_props.setProperty("height", Integer.toString(height));
	}
	
	public int getX(){
		return  Integer.valueOf(m_props.getProperty("x"));
	}
	
	public void setX(final int x){
		m_props.setProperty("x", Integer.toString(x));
	}
	
	public int getY(){
		return Integer.valueOf(m_props.getProperty("y"));
	}
	
	public void setY(final int y){
		m_props.setProperty("y", Integer.toString(y));
	}
	

}
