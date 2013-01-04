package superlines.ws;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

public class FilesResponse extends BaseResponse{

	public void setFiles(final Set<String> files){
		m_files = files;
	}
	
	public Set<String> getFiles(){
		return m_files;
	}
	
	@XmlElement(name="files")
	private Set<String> m_files;
}
