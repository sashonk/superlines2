package superlines.core;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Localizer {
	
	private static final Log log = LogFactory.getLog(Localizer.class);
	private static Map<String, Properties> m_localizations = new HashMap<>(); 
	

	public static String getLocalizedString(final Object key, final String preferredLocale){
		Properties localization = m_localizations.get(preferredLocale);
		if(localization==null){
			Configuration cfg =  Configuration.get();
			File folder = cfg.getDataFolder();
			File locFile = new File(folder, String.format("localization-%s.properties",preferredLocale));			
			FileInputStream ifstr = null;
			try{
				localization = new Properties();
				 ifstr = new FileInputStream(locFile);		
				 localization.load(ifstr);
			}
			catch(Exception ex){
				log.error(ex);
			}
			finally{
				if(ifstr!=null){
					try{
						ifstr.close();
					}
					catch(Exception t){
						log.error(t);
					}
				}
			}
			
			m_localizations.put(preferredLocale, localization);
		}
		
		return localization.getProperty(key.toString());
	}
	
	public static String getLocalizedString(final Object key){
		
		return getLocalizedString(key, Configuration.get().getProperty("locale"));
	}
}
