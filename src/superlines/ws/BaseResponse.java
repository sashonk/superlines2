package superlines.ws;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseResponse {

	public Message getMessage(){
		return message;
	}
	
	public void setMessage(final Message m){
		message = m;
	}
	
	@XmlElement(name="message")
	private Message message;
}
