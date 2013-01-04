package superlines.client.ws;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import superlines.core.Authentication;
import superlines.core.Configuration;
import superlines.core.Rank;
import superlines.core.SuperlinesContext;
import superlines.core.SuperlinesRules;
import superlines.core.Profile;
import superlines.ws.BaseResponse;
import superlines.ws.BinaryResponse;
import superlines.ws.FilesResponse;
import superlines.ws.PromotionResponse;
import superlines.ws.Response;
import superlines.ws.RateData;
import superlines.ws.RateParameters;
import superlines.ws.RateResponse;
import superlines.ws.SuperlinesWebservice;
import superlines.ws.ProfileResponse;


/**
 * adapter for superlines web service. 
 * 
 * @author Sashonk
 *
 */

public class ServiceAdapter {
	private static Log log = LogFactory.getLog(ServiceAdapter.class);
	private static ServiceAdapter instance;
	private SuperlinesWebservice webservice;
	
	/**
	 * 
	 * @return web service adapter or <b>null</b> if web service is unavailable
	 */
	public static ServiceAdapter get(){
		if(instance ==null){
			try{
				instance = new ServiceAdapter();
			}
			catch(Exception e){
				log.error("failed to create Invoker", e);
			}
		}
		
		return instance;
	}
	
	private ServiceAdapter() throws Exception{
		Configuration cfg = Configuration.get();
		String url = cfg.getProperty("service.url");
		
		SuperlinesWebserviceImplService service = new SuperlinesWebserviceImplService(new URL(url));
		webservice = service.getPort(SuperlinesWebservice.class);		
	}
	
	public SuperlinesWebservice getService(){
		return webservice;
	}
	
	public void acceptResult(final Authentication auth, final int score){
		BaseResponse response = webservice.acceptResult(auth, score);
		if(response.getMessage()!=null){
			log.error(response.getMessage());
		}
	}

	
	public Profile getProfile(final Authentication auth){
		ProfileResponse res = webservice.getProfile(auth);
		if(res.getMessage()!=null){
			log.error(res.getMessage());
		}
		
		return res.getProfile();
	}
        
        public SuperlinesRules getRules(final Authentication auth){
            Response<SuperlinesRules> res = webservice.getRules(auth);
            if(res.getMessage()!=null){
                log.error(res.getMessage());                
            }
            
            return res.getValue();
        }
        
        public Profile getUser(final Authentication auth){
            ProfileResponse res = webservice.getProfile(auth);
            if(res.getMessage()!=null){
                log.error(res.getMessage());                
            }
            
            return res.getProfile();
        }
        
        public byte[] getSuperlinesContext(final Authentication auth, final boolean createOnly){
        	BinaryResponse res = webservice.getSuperlinesContext(auth, createOnly);
            if(res.getMessage()!=null){
                log.error(res.getMessage());                
            }
            
            return res.getData();          
        }
        
        public List<RateData> getRateData(final Authentication auth, final RateParameters params){
        	RateResponse res = webservice.getRateData(auth, params);
            if(res.getMessage()!=null){
                log.error(res.getMessage());                
            }
            
            return res.getData();     	
        }
        
        public String getPromotionMessage(final Authentication auth, final Rank rank , final String locale){
        	PromotionResponse response = webservice.getPromotionMessage(auth, rank, locale);
        	if(response.getMessage()!=null){
        		log.error(response.getMessage());
        	}
        	return response.getPromotionMessage();
        }
        
        public void persist(final Authentication auth, final byte[] ctxBytes){
        	BaseResponse response = webservice.persist(auth, ctxBytes);
        	if(response.getMessage()!=null){
        		log.error(response.getMessage());
        	}
        
        }
	
        
        /////////////////  UPDATE ////////////////////
        
        public byte[] getChecksumDocument(final String dir){
        	BinaryResponse response = webservice.getChecksumDocument(dir);
        	if(response.getMessage()!=null){
        		log.error(response.getMessage());
        	}
        	return response.getData();
        }
        
        public byte[] getFile(final String filePath){
        	BinaryResponse response = webservice.getFile(filePath);
        	if(response.getMessage()!=null){
        		log.error(response.getMessage());
        	}
        	return response.getData();
        }
        
        public Set<String> listFiles(final String dirPath){
        	FilesResponse response = webservice.listFiles(dirPath);
        	if(response.getMessage()!=null){
        		log.error(response.getMessage());
        	}
        	
        	return response.getFiles();
        }
}
