package superlines.server.mail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import superlines.core.Configuration;

public class MailHelper {
	
	private static  Log log = LogFactory.getLog(MailHelper.class);
	private static  String mailFromAddress;
	private static  String mailHost;
	private static  PopupAuthenticator m_auth;
	
	public static void initialize(){
		Configuration cfg = Configuration.get();
		mailFromAddress = cfg.getProperty("mail.from.address");
		mailHost = cfg.getProperty("mail.host");		
		m_auth = new PopupAuthenticator(cfg.getProperty("mail.from.login"), cfg.getProperty("mail.from.password"));
	}
	
	public static void mail(final String subject, final String body, final List<String> recipients){
		Properties props = new Properties();

		props.put("mail.smtp.host", mailHost);
		props.put("mail.smtp.auth", "true");
		
		
        Session session = Session.getInstance(props, m_auth);
        Message msg = new MimeMessage(session);
        try {
           
            msg.setFrom(new InternetAddress(mailFromAddress));
            
            for(String recipient : recipients){
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(recipient));           	
            }

            msg.setSubject(subject);            
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html; charset=\"UTF-8\"");
       
            
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            msg.setText(body);
            

        	Transport.send(msg);
        	log.debug("message sent, recipients: " + Arrays.toString(recipients.toArray()));
        } catch (Throwable e) {
        	log.error("failed send message, recipients: "+ Arrays.toString(recipients.toArray()) ,e);
        } 
	}

}
