package superlines.client;

import java.util.List;

import superlines.ws.RateData;

public interface RateListener {
	public void updateData(final List<RateData> data);
}
