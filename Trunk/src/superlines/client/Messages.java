package superlines.client;

import superlines.core.Localizer;

public enum Messages {
	TOSCORE,
	SCATTER,
	RESTART,
	TOGAME,
	
	OK,
	OFFLINE,
	
	LOGIN_LABEL,
	PASSWORD_LABEL,
	
	RATE_LABEL,
	SCORE_LABEL,
	RANK_LABEL,
	
	GAME_OVER,
	SURE,
	
	CONGRATULATIONS,
	PROGRESSIVE1_OPENED,
	PROGRESSIVE2_OPENED,	
	
	AUTH_FAILED,
	GENERIC_ERROR,
	SERVICE_UNAVAILABLE,
	NOT_ENOUGH_SCORE,
	SUBMIT_RESULT,
	
	UPDATE,
	RELAUNCH,
	CLOSE;
	

	
	@Override
	public String toString(){
		String key = String.format("%s.%s", Messages.class.getName(), this.name());
		String value = Localizer.getLocalizedString(key);
		if(value == null){
			value = key;
		}
		
		return value;
	}
}
