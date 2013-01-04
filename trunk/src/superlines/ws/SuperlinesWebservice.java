package superlines.ws;

import java.io.File;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.w3c.dom.Document;

import superlines.core.Authentication;
import superlines.core.Rank;
import superlines.core.SuperlinesContext;
import superlines.core.SuperlinesRules;



@WebService
public interface SuperlinesWebservice {
	
	@WebMethod
	public ProfileResponse getProfile(@WebParam(name="auth") final Authentication auth);
	
	@WebMethod
        public BinaryResponse getSuperlinesContext(@WebParam(name="auth") final Authentication auth, @WebParam(name="createOnly") final boolean createOnly);
        
        @WebMethod
        public Response<SuperlinesRules> getRules(@WebParam(name="auth") final Authentication auth);
        
        @WebMethod
        public RateResponse getRateData(@WebParam(name="auth") final Authentication auth, @WebParam(name="params") final RateParameters params);
	
        @WebMethod
        public BaseResponse acceptResult(@WebParam(name="auth") final Authentication auth, @WebParam(name="score") final int score);
        
        @WebMethod
        public PromotionResponse getPromotionMessage(@WebParam(name="auth") final Authentication auth, @WebParam(name="rank") final  Rank rank, @WebParam(name="locale") final String locale);
        

        @WebMethod
        public BaseResponse persist(@WebParam(name="auth") final Authentication auth ,@WebParam(name="context") final byte[] ctxBytes);
        
    	@WebMethod
        public BinaryResponse loadSuperlinesContext(@WebParam(name="auth") final Authentication auth);
    	
    	
    	//////////////////////  UPDATE  ////////////////////////
    	
    	////// example: dirName = classes
    	@WebMethod
    	public BinaryResponse getChecksumDocument(@WebParam(name="directory") final String dirName);
    	
    	////// example: dirName = classes\\superlines\\client\\ColorHelper.class
    	@WebMethod
    	public BinaryResponse getFile(@WebParam(name="directory") final String filePath);
    	
    	////// example: dirName = classes\\superlines\\client	
    	@WebMethod
    	public FilesResponse listFiles(@WebParam(name="directory") final String dirPath );
}
