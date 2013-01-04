package superlines.client.online;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import superlines.client.RateController;
import superlines.client.RatePanelModel;
import superlines.client.ws.ServiceAdapter;
import superlines.core.Authentication;
import superlines.ws.RateData;


public class RateControllerImpl implements RateController{
	private final static Log log = LogFactory.getLog(RateControllerImpl.class);
	private RatePanelModel m_model;
	private Authentication m_auth;
	
	@Override
	public void setModel(final RatePanelModel model){
		m_model = model;
	}
	
	@Override
	public void update() {
		List<RateData> data = ServiceAdapter.get().getRateData(m_auth, null);
		if(data==null){
			log.error("failed get score data");
			return;
		}
			
		m_model.setData(data);		
	}
	
	public void setAuth(final Authentication auth){
		m_auth = auth;
	}
	
	public RateControllerImpl(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(true){
					try{
						Thread.sleep(5000);
						SwingUtilities.invokeAndWait(new Runnable() {
							
							@Override
							public void run() {
								RateControllerImpl.this.update();
								
							}
						});
					}
					catch(Exception ex){
						
					}

				}
				
			}
		});
		
		t.start();
	}

}
