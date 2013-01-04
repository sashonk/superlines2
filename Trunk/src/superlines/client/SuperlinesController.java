package superlines.client;

import superlines.core.SuperlinesContext;

public interface SuperlinesController {
		
	public void spotClicked(int x, int y);
	
	public void scatter();
	
	public void restart();
	
    public SuperlinesContext getContext();
    

}
