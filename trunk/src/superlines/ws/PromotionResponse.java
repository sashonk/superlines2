package superlines.ws;

import javax.xml.bind.annotation.XmlElement;

public class PromotionResponse extends BaseResponse {

	
	public void setPromotionMessage(final String value){
		m_message= value;
	}
	
	public String getPromotionMessage(){
		return m_message;
	}
				
	@XmlElement(name="promotionMessage")
	private String m_message;
}
