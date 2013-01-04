package superlines.ws;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import superlines.core.Rank;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RateParameters {

	public Rank getRank(){
		return m_rank;
	}
	
	public void setRank(final Rank rank){
		m_rank = rank;
	}
	
	public Date getBeginDate(){
		return m_begin;
	}
	
	public void setBeginDate(final Date value){
		m_begin = value;
	}
	
	public Date getEndDate(){
		return m_end;
	}
	
	public void setEndDate(final Date value){
		m_end = value;
	}
	
	public boolean isOwn(){
		return m_own;
	}
	
	public void setOwn(final boolean value){
		m_own = value;
	}
	
	public int getCount(){
		return m_count;
	}
	
	public void setCount(final int count){
		m_count= count;
	}
	
	@XmlElement(name="own", defaultValue="true")
	private boolean m_own;
	
	@XmlElement(name="endDate", nillable=true)
	private Date m_end;
	
	@XmlElement(name="beginDate", nillable=true)
	private Date m_begin;
	
	@XmlElement(name="rank", nillable=true)
	private Rank m_rank;
	
	@XmlElement(name="count", defaultValue="20")
	private int m_count;
}
