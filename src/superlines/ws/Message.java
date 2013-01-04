/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package superlines.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sashonk
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {
    
    public void setText(final String value){
        m_text = value;
    }
    
    public void setDetails(final String value){
        m_details = value;
    }
    
    public String getText(){
        return m_text;
    }
    
    public String getDetails(){
        return m_details;
    }
    
    @Override
    public String toString(){
    	return String.format("message:{text=%s, details=%s}", m_text, m_details);
    }
    
    @XmlElement(name="details")
    private String m_details;
    
    @XmlElement(name="text")
    private String m_text;
}
