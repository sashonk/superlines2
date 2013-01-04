package superlines.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import superlines.core.Rank;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RateData {

	public String getName(){
		return m_username;
	}
	
	public void setName(final String name){
		m_username = name;
	}
	
	public int getScore(){
		return m_score;
	}
	
	public void setScore(final int score){
		m_score= score;
	}
	
	public void setRank(final Rank rank){
		m_rank = rank;
	}
	
	public Rank getRank(){
		return m_rank;
	}
	
	
	@XmlElement(name="score")
	private int m_score;
	
	@XmlElement(name="username")
	private String m_username;
	
	@XmlElement(name="rank")
	private Rank m_rank;
}
