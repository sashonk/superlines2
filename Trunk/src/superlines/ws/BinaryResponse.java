package superlines.ws;

import javax.xml.bind.annotation.XmlElement;

public class BinaryResponse extends BaseResponse{
	public void setData(final byte[] value){
		m_data = value;
	}
	
	public byte[] getData(){
		return m_data;
	}
	
	@XmlElement(name="data")
	private byte[] m_data;
}
