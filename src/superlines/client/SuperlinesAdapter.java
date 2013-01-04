package superlines.client;

import java.util.List;

import superlines.core.SuperlinesContext;
import superlines.core.SuperlinesListener;
import superlines.core.SuperlinesBall.State;

public class SuperlinesAdapter implements SuperlinesListener{

	@Override
	public void scoreChanged(int newScore, int oldScore) {}

	@Override
	public void ballChangeColor(int x, int y, int newCol, int oldCol) {}

	@Override
	public void ballChangeState(int x, int y, State newState, State oldState) {}

	@Override
	public void clickedBallChanged(int newx, int newy, int oldx, int oldy) {}

	@Override
	public void clickeBallSet(int x, int y) {}

	@Override
	public void clickedBallUnset(int x, int y) {}

	@Override
	public void tableFilled(final int score) {}

	@Override
	public void nextColorsChanged(final List<Integer> data) {}

	@Override
	public void init(final SuperlinesContext ctx) {}
	
	@Override
	public void postTableFilled(){}

	@Override
	public void progressiveOpened(int level) {}

}
