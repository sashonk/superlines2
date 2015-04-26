package superlines.server.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
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

		props.put("mail.smtps.host", mailHost);
		props.put("mail.smtps.auth", "true");
		props.put("mail.smtps.starttls.enable", "true");
		props.put("mail.smtps.ssl.enable", "true");
		
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
            
            Transport t =  session.getTransport("smtps");

            t.connect();
            msg.saveChanges(); 
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
            
        	log.debug("message sent, recipients: " + Arrays.toString(recipients.toArray()));
        } catch (Throwable e) {
        	String sepa = System.getProperty("line.separator");
        	StringBuilder errorBuilder = new StringBuilder("email send failed:").append(sepa); 
        	errorBuilder.append("from=").append(mailFromAddress).append(sepa);        	
        	errorBuilder.append("to=[").append(Arrays.toString(recipients.toArray())).append("]").append(sepa); 
        	errorBuilder.append("host=").append(mailHost).append(sepa);        	
        	log.error(errorBuilder.toString(), e);
        } 
	}

}
