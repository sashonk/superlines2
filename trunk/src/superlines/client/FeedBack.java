package superlines.client;

public interface FeedBack {
	
	public void begin();
	
	public void updateTitle(final String newValue);
	
	public void updateProgress(float p);

	public void end();
}
