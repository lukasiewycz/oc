package de.timedout.oc.browser.model;

import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Image;

public interface Element {

	public Long getFilesize();
	
	public String getFilename();
	
	public boolean isDirectory();
	
	public Image getIcon();
	
	public boolean isReadable();
	
	public boolean isHidden();

	public void addPropertyChangeListener(PropertyChangeListener l);
	
	public void removePropertyChangeListener(PropertyChangeListener l);
	
}
