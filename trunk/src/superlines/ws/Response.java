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
public class Response<T> {
    
    public void setValue(final T value){
        m_value = value;
    }
    
    public T getValue(){
        return m_value;
    }

	public Message getMessage(){
		return message;
	}
        
        public void setMessage(final Message value){
            message = value;
        }
	
        @XmlElement(name="message")
	private Message message;
        
        @XmlElement(name="value")
        
        private T m_value;

}
