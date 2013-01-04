package superlines.core;


public interface ProfileListener {
	public void nameChanged(final String newName);
	
	public void rankChanged(final Rank newRank);
	
	public void rateChanged(final int newRate);
	
	public void init(final Profile p);
}
