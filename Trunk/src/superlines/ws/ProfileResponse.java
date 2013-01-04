package superlines.ws;

import javax.xml.bind.annotation.XmlElement;

import superlines.core.Profile;

public class ProfileResponse extends BaseResponse{

	public Profile getProfile(){
		return user;
	}
	
	public void setProfile(final Profile u){
		user = u;
	}
	
	@XmlElement(name="user")
	private Profile user;
}
