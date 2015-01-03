package de.timedout.oc.browser.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public abstract class AbstractElement implements Element {

	final private PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener l){
		support.addPropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener l){
		support.removePropertyChangeListener(l);
	}
	
	public void fireModify(){
		support.firePropertyChange("v", 0, 1);
	}
	

}
