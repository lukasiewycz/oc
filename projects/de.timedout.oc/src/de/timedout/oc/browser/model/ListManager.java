package de.timedout.oc.browser.model;

import ca.odell.glazedlists.EventList;

public interface ListManager {
	
	public void init(EventList<Element> list, Element location);
	
	public void connect();
	
	public void disconnect();

}
