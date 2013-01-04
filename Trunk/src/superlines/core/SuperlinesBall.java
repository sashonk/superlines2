package superlines.core;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SuperlinesBall  implements Serializable{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6055325616122425355L;

	public enum State{
		NORMAL,
		CLICKED;	
	}

	public int getColor(){
		return m_color;
	}
	
	public void setColor(int color){
		int oldval = m_color;
		m_color = color;
                                                              		
		for(SuperlinesListener l : m_table.getContext().getListeners()){
			l.ballChangeColor(m_x, m_y, m_color, oldval);
		}
                          
	}
	
	public State getState(){
		return m_state;
	}
	
	public void setState(State s){
		State old = m_state;
		m_state = s;
		
		for(SuperlinesListener l : m_table.getContext().getListeners()){
			l.ballChangeState(m_x, m_y, m_state, old);
		}
	}
	
	public void setX(int x){
		m_x= x;
	}
	
	public void setY(int y){
		m_y =y;
	}
	
	public int getX(){
		return m_x;
	}
	
	public int getY(){
		return m_y;
	}
	
	public void setTable(final SuperlinesTable t){
		m_table = t;
	}
	
	public SuperlinesTable getTable(){
		return m_table;
	}
	
	@XmlElement(name="state")
	private State m_state;
	
	@XmlElement(name="color")
	private int m_color;
	
	@XmlAttribute(name="x")
	private int m_x;
	
	@XmlAttribute(name="y")
	private int m_y;
	
	@XmlTransient
	private SuperlinesTable m_table;

}
