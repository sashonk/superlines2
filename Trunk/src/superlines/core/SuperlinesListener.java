package superlines.core;

import java.util.List;

import superlines.core.SuperlinesBall.State;



public interface SuperlinesListener {
	
	public void scoreChanged(int newScore, int oldScore);

	public void ballChangeColor(int x, int y, int newCol, int oldCol);
	
	public void ballChangeState(int x, int y, State newState, State oldState);
	
	public void clickedBallChanged(int newx, int newy, int oldx, int oldy);
	
	public void clickeBallSet(int x, int y);
	
	public void clickedBallUnset(int x, int y);
        
    public void tableFilled(final int score);
    
    public void postTableFilled();
    
    public void nextColorsChanged(final List<Integer> data);
    
    public void init(final SuperlinesContext ctx);
    
    public void progressiveOpened(final int level);
}
