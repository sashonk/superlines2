package superlines.core;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="User")
public class Profile {
	
	public void registerListener(final ProfileListener l){
		m_listeners.add(l);
	}

	public Authentication getAuth(){
		return m_auth;
	}
	
	public void setAuth(final Authentication value){
		m_auth = value;
	}

	
	public String getUsername(){
		return m_username;
	}
	
	public void setUsername(final String value){
		m_username = value;
		for(ProfileListener l : m_listeners){
			l.nameChanged(value);
		}		
	}
	
	public Rank getRank(){
		return m_rank;
	}
	
	public void setRank(final Rank rank){
		m_rank = rank;
		for(ProfileListener l : m_listeners){
			l.rankChanged(m_rank);
		}
	}

	public Date getCreateDate(){
		return m_crts;		
	}
	
	public void setCreateDate(final Date date){
		m_crts = date;
	}
	
	public int getRate(){
		return m_currentRate;
	}
	
	public void setRate(final int rate){
		m_currentRate = rate;
		for(ProfileListener l : m_listeners){
			l.rateChanged(rate);
		}
	}
	
	@XmlElement(name="auth", nillable=true)
	private Authentication m_auth;
	
	@XmlElement(name="username")
	private String m_username;
	
	@XmlElement(name="rank")
	private Rank m_rank;
	
	@XmlAttribute(name="crts")
	private Date m_crts;
	
	@XmlElement(name="rate")
	private int m_currentRate;
	
	@XmlTransient
	private List<ProfileListener> m_listeners = new LinkedList<>();
	
}
