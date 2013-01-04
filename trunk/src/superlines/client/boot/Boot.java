package superlines.client.boot;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.concurrent.Executors;


import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.*;
import org.apache.log4j.PropertyConfigurator;


import superlines.client.Application;
import superlines.client.Messages;
import superlines.client.ProfileController;
import superlines.client.RateController;
import superlines.client.RatePanelModel;
import superlines.client.SuperlinesController;
import superlines.client.offline.SuperlinesOfflineController;
import superlines.client.online.Persister;
import superlines.client.online.ProfileControllerImpl;
import superlines.client.online.RateControllerImpl;
import superlines.client.online.ScoreSender;
import superlines.client.online.SuperlinesControllerImpl;
import superlines.client.online.Updater;
import superlines.client.ui.LoginFrame;
import superlines.client.ui.MainFrame;
import superlines.client.ui.PlayPanel;
import superlines.client.ui.RatePanel;
import superlines.client.ui.UpdateFrame;
import superlines.client.ws.ServiceAdapter;
import superlines.core.Authentication;
import superlines.core.Configuration;
import superlines.core.Rank;
import superlines.core.RulesHelper;
import superlines.core.SuperlinesContext;
import superlines.core.Profile;




public class Boot {
	
	private static final String LOG_CONFIG_PATH = "config/client/log4j.properties";
	private static final String APPLICATION_CONFIG_PATH = "config/client/boot.properties";
	private static final String DATA_FOLDER = "data/client";
	
	private static final Log log = LogFactory.getLog(Boot.class);
	
	private static LookAndFeelInfo findLookAndFeel(final String name){
		 for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				// System.out.println(info.getName());
	        if (name.equals(info.getName())) {
	            return info;
	        }
	    }
		 
		 return null;
	}
    
    public static void main(String[] argc) throws Exception{
        	Thread.currentThread().setName("main-thread");    	
    		System.setProperty("config.file.path",APPLICATION_CONFIG_PATH);
    		System.setProperty("data.folder", DATA_FOLDER); 

    		PropertyConfigurator.configure(LOG_CONFIG_PATH);
    	
    		log.debug("application start");
    		
    		Configuration cfg = Configuration.get();
    		String lnfName = cfg.getProperty("preferred.lookandfeel.name");
    		
    		if(lnfName!=null){
    			LookAndFeelInfo info = findLookAndFeel(lnfName);
 			   try {  
 				   UIManager.setLookAndFeel(info.getClassName());

 			   } 
 				catch (Exception e) {
 					log.error(e);
 				}
    		}
    		

        
            SwingUtilities.invokeAndWait(new Runnable(){

            @Override
            public void run() {
                

                
               final LoginFrame loginFrame = new LoginFrame();   
         
                        
                        

   
        loginFrame.getOkButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                
                try{
                	                	 
                LoginFrame d =loginFrame;                
                Authentication auth = new Authentication();
                d.writeData(auth);
                auth.setLocale(Configuration.get().getProperty("locale"));
                
                if(auth.getLogin()==null || auth.getPassword()==null){
                    return;
                }
                d.setErrorMessage("");
                
                ServiceAdapter adapter = ServiceAdapter.get();                
                if(adapter==null){
                    d.setErrorMessage(Messages.SERVICE_UNAVAILABLE.toString());
                    return;
                }
                
                              
                Profile profile = adapter.getProfile(auth);
                if(profile==null){                    
                    d.setErrorMessage(Messages.AUTH_FAILED.toString());
                    return;
                }
                
                
                // create GUI
                final  MainFrame frame = MainFrame.get();
                final RatePanel scorePanel = new RatePanel();
                final PlayPanel playPanel = new PlayPanel();
                frame.addPanel(scorePanel);
                frame.addPanel(playPanel);
                
                
                final Updater up = Updater.get();
                if(up.updatesAvailable()){
                	d.dispose();
                	final UpdateFrame upFrame = new UpdateFrame();
                	upFrame.setVisible(true);
                	
                	Executors.newSingleThreadExecutor().execute(new Runnable() {
						
						@Override
						public void run() {
							try{
								up.update(upFrame);
							}
							catch(Exception ex){
								log.error(ex);
							}
							
						}
					});
                	
                	
                	return;
                }
                
                
                ProfileControllerImpl profileCtr = new ProfileControllerImpl();
                profileCtr.setAuth(auth);
                profileCtr.setModel(profile);
                profile.registerListener(playPanel);
                playPanel.setController(profileCtr);
                playPanel.init(profile);

                SuperlinesControllerImpl ctr = new SuperlinesControllerImpl();               
                ctr.setAuth(auth);
                ctr.start();

                                            
                SuperlinesContext c = ctr.getContext();
                c.registerListener(playPanel);
                c.registerListener(profileCtr);
                playPanel.setController(ctr);               
                playPanel.init(c);
                
                Persister persister = new Persister();
                c.registerListener(persister);
                persister.setAuth(auth);
                persister.init(c);                
                frame.registerListener(persister);
    
                
                ScoreSender sender = new ScoreSender();
                sender.setAuth(auth);
                c.registerListener(sender);
                sender.init(c);
                

                RatePanelModel scoreModel = new RatePanelModel();
                scoreModel.registerListener(scorePanel);
                RateControllerImpl scoreCtr = new RateControllerImpl();
                scoreCtr.setAuth(auth);
                scoreCtr.setModel(scoreModel);
                scorePanel.setController(scoreCtr);
                scoreCtr.update();                                
                d.dispose();
                
                frame.setVisible(true);
                frame.showNamedPanel(Messages.TOGAME.toString());
                }
                catch(Exception ex){
                    log.error(ex);
                    Application.exit(1);
                }
            }
        });
        
        
        loginFrame.getOfflineButton().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// create GUI
                final  MainFrame frame = MainFrame.get();
                final RatePanel scorePanel = new RatePanel();
                final PlayPanel playPanel = new PlayPanel();
                frame.addPanel(scorePanel);
                frame.addPanel(playPanel);

				
				Profile profile = new Profile();
				profile.setRate(0);
				profile.setCreateDate(new Date());
				profile.setRank(Rank.NEWBIE);
				profile.setUsername("Anonimous");
												
				playPanel.init(profile);
				
				SuperlinesOfflineController ctr = new SuperlinesOfflineController();
				ctr.start();
				playPanel.setController(ctr);
				
				SuperlinesContext ctx = ctr.getContext();
				ctx.registerListener(playPanel);
				playPanel.init(ctx);
				
                loginFrame.dispose();
                
                frame.setVisible(true);
                frame.showNamedPanel(Messages.TOGAME.toString());		
			}
        	
        });
        
        loginFrame.setVisible(true);
        
            }
            } );
            
    }}

            
        
            
            


    


