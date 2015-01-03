package de.timedout.oc.browser.cells;

import org.eclipse.swt.graphics.Image;

import de.timedout.oc.browser.model.Element;

public interface CellData<T> {

	public T getContent();
	
	public Image getIcon();
	
	public String getText();
	
	public Element getFile();
	
}
