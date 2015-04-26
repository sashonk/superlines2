package superlines.server.ws;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import superlines.server.mail.MailHelper;

public class SuperlinesContextListener implements ServletContextListener{

	private static final Log log = LogFactory.getLog(SuperlinesContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		
		  ServletContext servletCtx = event.getServletContext();
		  String webContentFolder = servletCtx.getRealPath("/");
		  System.setProperty("config.file.path", String.format("%s%s", webContentFolder, "WEB-INF/config/config.properties"));
		  System.setProperty("data.folder", String.format("%s/%s", webContentFolder, "WEB-INF/data"));		  	
		  MailHelper.initialize();

		  log.debug("superlines web application initialized");
	}	

}
