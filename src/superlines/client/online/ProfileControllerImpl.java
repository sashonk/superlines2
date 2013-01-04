package superlines.client.online;

import javax.swing.JOptionPane;

import superlines.client.ProfileController;
import superlines.client.SuperlinesAdapter;
import superlines.client.ui.MainFrame;
import superlines.client.ws.ServiceAdapter;
import superlines.core.Authentication;
import superlines.core.Configuration;
import superlines.core.Profile;

public class ProfileControllerImpl  extends SuperlinesAdapter implements ProfileController{
	
	private Authentication m_auth;
	private Profile m_model;
	
	@Override
	public Profile getModel(){
		return m_model;
	}
	
	public void setModel(final Profile p){
		m_model = p;
	}
	
	public void setAuth(final Authentication auth){
		m_auth = auth;
	}
	
	@Override
	public void postTableFilled(){
		Profile p = ServiceAdapter.get().getProfile(m_auth);
		if(m_model.getRank()!= p.getRank()){
			
			String str = ServiceAdapter.get().getPromotionMessage(m_auth, p.getRank(), Configuration.get().getProperty("locale"));
			
			if(str!=null){
				JOptionPane.showMessageDialog(MainFrame.get(), str);
			}
						
			m_model.setRank(p.getRank());

		}
		if(m_model.getRate()!=p.getRate()){
			m_model.setRate(p.getRate());
		}
	}
	

}
