package superlines.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class SuperlinesContext implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5626972482701147748L;

	public void registerListener(final SuperlinesListener l){
		getListeners().add(l);
	}
	
	public List<SuperlinesListener> getListeners(){		
		if(m_listeners==null){
			m_listeners = new LinkedList<>();
		}
		return m_listeners;
	}
	

		
	public void setRules(SuperlinesRules value){
		m_rules = value;
	}
	
	public SuperlinesRules getRules(){
		return m_rules;				
	}
	
	public SuperlinesTable getTable(){
		return m_table;
	}
	
	public int getScore(){
		return m_score;
	}
	
	public void setScore(int value){
		int oldval = m_score;
		m_score = value;		
		for(SuperlinesListener l : getListeners()){
			l.scoreChanged(m_score, oldval);
		}

		if(m_rules.isProgressiveEnabled()){
			if(m_score > m_rules.getProgressive2Threshold() && !m_progressive2Notified){
				m_progressive1Notified = true;
				m_progressive2Notified = true;
				for(SuperlinesListener l : getListeners()){
					l.progressiveOpened(2);
				}
				
			}
			else if(m_score > m_rules.getProgressive1Threshold() && !m_progressive1Notified){
				m_progressive1Notified = true;
				for(SuperlinesListener l : getListeners()){
					l.progressiveOpened(1);
				}				
			}
		}
	}
	
	public void setTable(final SuperlinesTable value){
		m_table= value;
	}
        
    public List<Integer> getNextColors(){
            return m_nextColors;
    }
    
    public void setNextColors(final List<Integer> nextColors){
    	m_nextColors = nextColors;
    	for(SuperlinesListener l : getListeners()){
    		l.nextColorsChanged(m_nextColors);
    	}
    }
    
    public void touch(){
    	for(SuperlinesListener l : getListeners()){
    		l.init(this);
    	}
    }
	
	@XmlElement(name="table",nillable=true)
	private SuperlinesTable m_table;
	
	@XmlElement(name="rules", nillable=true)
	private SuperlinesRules m_rules;
	
	@XmlElement(name="score")
	private int m_score;
	

	private transient List<SuperlinesListener> m_listeners;

        @XmlElementWrapper(name="nextColors",nillable=true)
        @XmlElement(name="color", nillable=true)
        private List<Integer> m_nextColors = new LinkedList<Integer>();
    
    @XmlTransient
    private boolean m_progressive1Notified;
    
    @XmlTransient
    private boolean m_progressive2Notified;
}
