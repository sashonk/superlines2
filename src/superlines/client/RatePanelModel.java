package superlines.client;

import java.util.LinkedList;
import java.util.List;

import superlines.ws.RateData;

public class RatePanelModel {
	
	public void registerListener(final RateListener listener){
		m_listeners.add(listener);
	}

	public List<RateData> getData(){
		return m_data;				
	}
	
	public void setData(final List<RateData> data){
		m_data = data;
		for(RateListener listener : m_listeners){
			listener.updateData(m_data);
		}
	}
	
	private List<RateData> m_data;
	private List<RateListener> m_listeners = new LinkedList<RateListener>();
}
