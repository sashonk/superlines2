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
public class SuperlinesTable  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5976355639858306132L;

	public void setWidth(int width){
		m_width = width;
		balls = new SuperlinesBall[width][];
		for(int i = 0; i<width; i++){
			balls[i] = new SuperlinesBall[width];
			for(int j = 0; j<width; j++){
				balls[i][j] = new SuperlinesBall();
				balls[i][j].setX(i);
				balls[i][j].setY(j);
				balls[i][j].setTable(this);
			}
		}		
	}
	
	public SuperlinesBall[][] getBalls(){
		return balls;
	}
	
	public SuperlinesBall getBall(int x, int y){
		return balls[x][y];
	}
	
	public int getSize(){
		return m_width;
	}
	
	public SuperlinesBall getClickedBall(){
		return m_clickedBall;
	}
	
	public void setClickedBall(final SuperlinesBall ball){
		SuperlinesBall old = m_clickedBall;
		m_clickedBall = ball;

		for(SuperlinesListener l : m_ctx.getListeners()){
			if(old==null){
				l.clickeBallSet(m_clickedBall.getX(), m_clickedBall.getY());
			}
			else if(ball==null){
				l.clickedBallUnset(old.getX(), old.getY());
			}
			else{
				l.clickedBallChanged(m_clickedBall.getX(), m_clickedBall.getY(), old.getX(), old.getY());
			}
		}
		
	}
	
	public void setContext(final SuperlinesContext c){
		m_ctx = c;
	}
	
	public SuperlinesContext getContext(){
		return m_ctx;
	}
        
/*    public void incColoredCount(int value){
            m_coloredCount+=value;
            
            if(m_coloredCount==m_width*m_width){
                for(SuperlinesListener l : m_ctx.getListeners()){
                    l.tableFilled(m_ctx.getScore());
                }
            }
        }*/
	
	
	public void setFilled(final boolean value){
		m_filled = value;
		if(m_filled){
	        for(SuperlinesListener l : m_ctx.getListeners()){
	            l.tableFilled(m_ctx.getScore());
	        }
		}
		if(m_filled){
	        for(SuperlinesListener l : m_ctx.getListeners()){
	            l.postTableFilled();
	        }			
		}		
	}
        

	
    public boolean isFilled(){
            return m_filled;
    }
	
	@XmlElement(name="balls", nillable=true)
	private SuperlinesBall[][] balls;
	
	@XmlAttribute(name="width")
	private int m_width;
	
	@XmlTransient
	private SuperlinesBall m_clickedBall;
	
	@XmlTransient
	private SuperlinesContext m_ctx;
        
    @XmlTransient
    private int m_coloredCount = 0;
    
    @XmlTransient
    private boolean m_filled;
        
        
}
