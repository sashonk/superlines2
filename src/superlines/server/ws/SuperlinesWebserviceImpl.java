package superlines.server.ws;


import superlines.server.PromotionDAO;
import superlines.server.RateDAO;
import superlines.server.RulesDAO;
import superlines.server.mail.MailHelper;
import superlines.ws.ProfileResponse;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import javax.sql.DataSource;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import superlines.core.Authentication;
import superlines.core.CheckSumGenerator;
import superlines.core.Configuration;
import superlines.core.Localizer;
import superlines.core.Rank;
import superlines.core.SuperlinesContext;
import superlines.core.SuperlinesRules;
import superlines.core.Util;

import superlines.core.Profile;

import superlines.ws.BaseResponse;
import superlines.ws.BinaryResponse;
import superlines.ws.FilesResponse;
import superlines.ws.Message;
import superlines.ws.PromotionResponse;
import superlines.ws.Response;

import superlines.ws.RateParameters;
import superlines.ws.RateResponse;
import superlines.ws.SuperlinesWebservice;


@WebService(endpointInterface="superlines.ws.SuperlinesWebservice")
@XmlAccessorType(XmlAccessType.FIELD)
public class SuperlinesWebserviceImpl implements SuperlinesWebservice{
	
	private final static Log log = LogFactory.getLog(SuperlinesWebserviceImpl.class);

	//@Resource(name="jdbc/mysql")
 	private DataSource m_dataSource;
 	

	public SuperlinesWebserviceImpl(){
		  try {
				Context ctx = new InitialContext();
				m_dataSource = (DataSource)ctx.lookup("java:comp/env/jdbc/mysql");
				
			  } catch (NamingException e) {
				  log.error(e);
			 }

		  
	}
	
	@Override
	@WebMethod
	public ProfileResponse getProfile(@WebParam Authentication ctx) {
		ProfileResponse r = new ProfileResponse();	
		Connection c = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			
		auth(ctx);

		 c = m_dataSource.getConnection();
		 
		st = c.prepareStatement("select p.id, p.name as name, p.rankid as rankid, p.crts as crts, (select sum(s.score) from scoredata as s where s.userid = p.id and s.rankid = p.rankid) as rate from profiles as p where accountid = ?");
		st.setString(1, ctx.getLogin());
		rs = st.executeQuery();
		
		if(rs.next()){
			Profile profile = new Profile();
			profile.setAuth(ctx);
			profile.setUsername(rs.getString("name"));
			profile.setRank(Rank.getRank(rs.getInt("rankid")));
			profile.setCreateDate(rs.getTimestamp("crts"));		
			profile.setRate(rs.getInt("rate"));
			r.setProfile(profile);
		}
		else{
			throw new Exception("profile not found");
		}
			
		
		
		
		}
		catch(Exception ex){
                    Message m = new Message();
                    m.setText("generic error");
                    m.setDetails(Util.toString(ex));
                    r.setMessage(m);
                    log.error(ex);
		}
		finally{

			try{
				if(st!=null){
					st.close();
				}
				if(c!=null){
					c.close();
				}
			}catch(Exception ex){
				log.error(ex);
			}
				
		}
		

		
		return r;
	}
	
	
	
	@Override
	@WebMethod
	public PromotionResponse getPromotionMessage(
			final Authentication auth,
			final Rank rank, final String locale) {
		
		PromotionResponse response = new PromotionResponse();
		try{
			String message = PromotionDAO.get().getPromotionMessage(rank, locale);
			response.setPromotionMessage(message);
			
		}
		catch(Exception x){
			Message m = new Message();
			m.setText("get promotion msg failed");
			m.setDetails(Util.toString(x));
			response.setMessage(m);
			log.error(x);
		}
		
		return response;
		
	}
	


    @Override
    public BinaryResponse getSuperlinesContext(Authentication auth, final boolean createOnly) {
    	BinaryResponse response = new BinaryResponse();
       try{
    	   ProfileResponse p = getProfile(auth);
    	   Profile profile = p.getProfile();
    	   
    	   
    	   if(!createOnly){
    		   BinaryResponse loadResponse =  loadSuperlinesContext(auth);
    		   byte[] loadedCtxBytes  = loadResponse.getData();
    		   if(loadedCtxBytes!= null){
    			   response.setData(loadedCtxBytes);
    		   }
    	   }
    	   
    	   if(response.getData()==null){
	    	   SuperlinesRules rules =  RulesDAO.get().createRules(profile.getRank());    	   
	           SuperlinesContext ctx = new SuperlinesContext();
	           ctx.setRules(rules);
	           ctx.setScore(0);	    
	           
	           ByteArrayOutputStream baos = new ByteArrayOutputStream();
	           ObjectOutputStream oos = new ObjectOutputStream(baos);
	           oos.writeObject(ctx);
	           response.setData(baos.toByteArray());    
    	   }
           
       }
       catch(Exception ex){
           Message m = new Message();
           m.setText("failed to create context");
           m.setDetails(Util.toString(ex));
           response.setMessage(m);
           log.error(ex);
       }
       
       return response;
    }
    
    @Override
    public Response<SuperlinesRules> getRules(Authentication auth){
        throw new UnsupportedOperationException("Not supported yet.");
    }
 

	@Override
	@WebMethod
	public BaseResponse acceptResult(final Authentication auth,
			final int score) {
		BaseResponse response = new BaseResponse();
		
		Connection c = null;
		PreparedStatement st= null;
		

		
		try{
			auth(auth);
				
				c = m_dataSource.getConnection();
				
				
				ResultSet rs;
				c.setAutoCommit(false);				
				st =c.prepareStatement("select id, rankid from profiles where accountid = ?");
				st.setString(1, auth.getLogin());
				rs = st.executeQuery();
				
				int id, rank;
				if(rs.next()){
					id = rs.getInt("id");
					rank = rs.getInt("rankid");
				}
				else{
					throw new Exception(String.format("profile not found for %s", auth.getLogin()));
				}
				st.close();
				
				int acceptableScore = RulesDAO.get().getAcceptableResult(Rank.getRank(rank));
				if(acceptableScore>score){
					Message m = new Message();
					m.setText(String.format("%s %d", Localizer.getLocalizedString(superlines.server.Messages.NOT_ENOUGH_SCORE, auth.getLocale()), acceptableScore));
					response.setMessage(m);
					return response;
				}
				
				
				st = c.prepareStatement("insert into scoredata (userid ,score, crts, rankid) values (?, ?, CURRENT_TIMESTAMP, ? )");
				st.setInt(1, id);
				st.setInt(2, score);
				st.setInt(3, rank);
				st.executeUpdate();
				st.close();
				
				
				st = c.prepareStatement("select sum(score) as total from scoredata where userid = ? and rankid = ?");
				st.setInt(1, id);
				st.setInt(2, rank);
				rs = st.executeQuery();
				rs.next();			
				int total = rs.getInt("total");
				st.close();
				
				Rank nextRank = Rank.getRank(rank).getNext();
				
				if(nextRank!=null){
					int targetRate = PromotionDAO.get().getQualifiedRate(nextRank);
					if(targetRate <= total ){
						st = c.prepareStatement("update profiles set rankid = ? where id = ?");
						st.setInt(1, nextRank.getRank());
						st.setInt(2, id);
						st.executeUpdate();				
					}
				}

				st.close();
				c.commit();
		}
		catch(Exception ex){
			try{
				c.rollback();
			}
			catch(Exception e){
				log.error(e);
			}
			
			Message m = new Message();
			m.setText("accept result failed");
			m.setDetails(Util.toString(ex));
			response.setMessage(m);
			log.error(ex);
		}
		finally{
			try{
				if(st!=null){
					st.close();
				}
				if(c!=null){
					c.close();
				}
			}catch(Exception ex){
				log.error(ex);
			}
		}
		
		return response;
		
	}


	@Override
	@WebMethod
	public RateResponse getRateData(@WebParam Authentication auth,
			@WebParam RateParameters params) {
		RateResponse response = new RateResponse();
		try{
			auth(auth);
			response.getData().addAll(RateDAO.get().getRateData(params)) ;
					
		}
		catch(Exception ex){
			Message m = new Message();
			m.setText("error getting rate data");
			m.setDetails(Util.toString(ex));
			response.setMessage(m);
			log.error(ex);
			return response;
		}

		return response;
	}
	
	
	@Override
	@WebMethod
	public BaseResponse persist(final Authentication auth , final byte[] ctxBytes){
		BaseResponse response = new BaseResponse();
		Connection conn = null;
		PreparedStatement st = null;
		
		try{
			auth(auth);
			
			conn = m_dataSource.getConnection();
			conn.setAutoCommit(false);

			Profile p = getProfile(auth).getProfile();		
			ByteArrayInputStream bais = new ByteArrayInputStream(ctxBytes);				

			
				st = conn.prepareStatement("delete from persistance where accountid = ? and rankid = ?");
				st.setString(1, auth.getLogin());
				st.setInt(2, p.getRank().getRank());
				st.executeUpdate();
				st.close();
								
		
				st = conn.prepareStatement("insert into persistance (accountid, superlinescontext, rankid) values (?, ?, ?)");
				st.setString(1, auth.getLogin());
				st.setBlob(2, bais);
				st.setInt(3, p.getRank().getRank());
				st.executeUpdate();
				
				st.close();
			
					
			
			conn.commit();
		}
		catch(Exception ex){
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error(conn);
			}
			Message m = new Message();
			m.setText("error persisting superlines context");
			m.setDetails(Util.toString(ex));
			response.setMessage(m);
			log.error(ex);
		}
		finally{
			try{
				if(st!=null){
					st.close();
				}
				if(conn!=null){
					conn.close();
				}
			}catch(Exception ex){
				log.error(ex);
			}
		}

		return response;
	}
	
	
	@WebMethod
	public BinaryResponse getFile(@WebParam(name="directory") final String filePath){
		BinaryResponse response = new BinaryResponse();
		FileInputStream ifstr = null;
		GZIPOutputStream gzip = null;
		try{
			File root =  new File(Configuration.get().getDataFolder(), "update");
			File file = new File(root, filePath);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 gzip = new GZIPOutputStream(baos);
											
			 ifstr = new FileInputStream(file);

			
			byte[] b = new byte[1024];
			int read = 0;
			
			while((read = ifstr.read(b)) > 0){
				gzip.write(b,0, read);
			}
			gzip.flush();
			gzip.finish();
			
			response.setData(baos.toByteArray());
			return response;						
		}
		catch(Exception ex){
			log.error(ex);
			Message m = new Message();
			m.setText("exception occured!");
			m.setDetails(Util.toString(ex));
			response.setMessage(m);
			return response;
		}
		finally{
			try{
				if(ifstr!=null){
					ifstr.close();
				}
			}
			catch(Exception ex){
				log.error(ex);
			}
			
			
			try{
				if(ifstr!=null){
					gzip.close();
				}
			}
			catch(Exception ex){
				log.error(ex);
			}
			
		}
	}
	
	@WebMethod
	public BinaryResponse getChecksumDocument(@WebParam(name="directory") final String dirName){
		BinaryResponse response = new BinaryResponse();
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream ostr = new ObjectOutputStream(baos);
			File update = new File(Configuration.get().getDataFolder(), "update");			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db =  dbf.newDocumentBuilder();
			Document doc = db.parse(new File(update, String.format("%s.xml", dirName)));
			
			ostr.writeObject(doc);
			response.setData(baos.toByteArray());
			return response;
		}
		catch(Exception ex){
			log.error(ex);
			Message m = new Message();
			m.setText("exception occured!");
			m.setDetails(Util.toString(ex));
			response.setMessage(m);
			return response;
		}
	}
	
	private void auth(final Authentication auth) throws Exception{
				
		Connection c = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			
			c = m_dataSource.getConnection();
			st = c.prepareStatement("select count(0) count from users u, user_roles r where u.user_name = r.user_name and u.user_name = ? and u.user_password = ?  and r.role_name = 'user'");
			st.setString(1, auth.getLogin());
			st.setString(2, auth.getPassword());
			 rs = st.executeQuery();
			
			int count = 0;
			if(rs.next()){
				count = rs.getInt("count");
			}
			 
			if(count != 1){
				throw new Exception("authentication failed");
			}
			

		}

		finally{
				try{
					if(st!=null){
						st.close();
					}
					if(c!=null){
						c.close();
					}
				}catch(Exception ex){
					log.error(ex);
				
				}
		}		
	}

	@Override
	@WebMethod
	public BinaryResponse loadSuperlinesContext(
			@WebParam(name = "auth") Authentication auth) {
		BinaryResponse response = new BinaryResponse();
		Connection conn = null;
		PreparedStatement st = null;
		
		try{
			auth(auth);
			conn = m_dataSource.getConnection();
			Profile p = getProfile(auth).getProfile();
			
			st = conn.prepareStatement("select accountid, rankid, superlinescontext ctx from persistance where accountid = ? and rankid = ?");
			st.setString(1, auth.getLogin());
			st.setInt(2, p.getRank().getRank());
			
			ResultSet rs = st.executeQuery();
			if(rs.next()){

				InputStream is = rs.getBinaryStream("ctx");		
				ObjectInputStream ois = new ObjectInputStream(is);
				SuperlinesContext ctx =  (SuperlinesContext) ois.readObject();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(ctx);
				
				
				response.setData(baos.toByteArray());
			}

						
		}
		catch(Exception ex){
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error(conn);
			}
			Message m = new Message();
			m.setText("error loading superlines context");
			m.setDetails(Util.toString(ex));
			response.setMessage(m);
			log.error(ex);
		}
		finally{

			try{
				if(st!=null){
					st.close();
				}
				if(conn!=null){
					conn.close();
				}
			}catch(Exception ex){
				log.error(ex);
			
			}
	
		}
			
		return response;
	}

	@Override
	@WebMethod
	public FilesResponse listFiles(@WebParam(name = "directory") String dirPath) {
		FilesResponse response = new FilesResponse();
		Set<String> fileNames = new HashSet<>();
		response.setFiles(fileNames);
		
		Configuration cfg = Configuration.get();
		File updateFolder =  new File(cfg.getDataFolder(), "update");
		File dir = new File(updateFolder, dirPath);
		
		File[] files = dir.listFiles();
		for(File file : files){
			if(file.isFile()){
				fileNames.add( file.getName());
			}
		}
		
		return response;
	}



}
