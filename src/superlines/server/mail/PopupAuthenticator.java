package superlines.server.mail;
import javax.mail.*;


class PopupAuthenticator extends Authenticator {
	
	 PopupAuthenticator(String user, String pass){
		m_user = user;
		m_pass = pass;
	}

  public PasswordAuthentication getPasswordAuthentication() {
   

    return new PasswordAuthentication(m_user,m_pass);
  }
  
   String getUser(){
	  return m_user;
  }
  
   String getPassword(){
	  return m_pass;
  }
  
  private String m_user;
  private String m_pass;
}