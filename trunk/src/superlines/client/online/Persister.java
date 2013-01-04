package superlines.client.online;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import superlines.client.FrameListener;
import superlines.client.SuperlinesAdapter;
import superlines.client.ws.ServiceAdapter;
import superlines.core.Authentication;
import superlines.core.SuperlinesContext;

public class Persister extends SuperlinesAdapter implements FrameListener{
	private static final Log log = LogFactory.getLog(Persister.class);
	
	private SuperlinesContext m_ctx;
	
	private Authentication m_auth;
	
	@Override
	public void init(final SuperlinesContext ctx){
		m_ctx = ctx;
	}
	
	public void setAuth(final Authentication auth){
		m_auth = auth;
	}
	
	@Override
	public void frameClosing() {
		
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(m_ctx);		
			ServiceAdapter.get().persist(m_auth, baos.toByteArray());
		}
		catch(Exception ex){
			log.error(ex);
		}
		
	}

}
