package superlines.client.online;

import javax.swing.JOptionPane;

import superlines.client.Messages;
import superlines.client.SuperlinesAdapter;
import superlines.client.ui.MainFrame;
import superlines.client.ws.ServiceAdapter;
import superlines.core.Authentication;
import superlines.core.SuperlinesContext;
import superlines.ws.BaseResponse;

public class ScoreSender extends SuperlinesAdapter{
	
	private Authentication m_auth;
	private SuperlinesContext m_ctx;
	
	public void setAuth(final Authentication auth){
		m_auth = auth;
	}
	

	
	@Override
	public void init(final SuperlinesContext ctx){
		m_ctx = ctx;
	}
	
	@Override
	public void tableFilled(final int score) {
		
		if(m_ctx.getRules().getMinScore()> score){
			JOptionPane.showMessageDialog(MainFrame.get(), String.format("%s %d", Messages.NOT_ENOUGH_SCORE, m_ctx.getRules().getMinScore()));
			return;
		}
		
		ServiceAdapter sa = ServiceAdapter.get();		
		sa.acceptResult(m_auth, score);
	
	}
}
