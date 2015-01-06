package de.timedout.oc.browser.model;

import ca.odell.glazedlists.EventList;

public interface LocationMediator {
	
	public static interface PopulateListener {
		public void finished();
	}
	
	public void init(EventList<Element> list, Element location);
	
	public void connect();
	
	public void disconnect();
	
	public void addPopulateListener(PopulateListener listener);

}
