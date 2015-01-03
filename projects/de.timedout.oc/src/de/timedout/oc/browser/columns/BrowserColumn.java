package de.timedout.oc.browser.columns;

import java.util.Comparator;

import de.timedout.oc.browser.cells.CellData;
import de.timedout.oc.browser.model.Element;

public interface BrowserColumn<T> {

	public CellData<T> getCell(Element file);
	
	public Comparator<CellData<T>> getComparator();
	
	public String getName();
	
	public int getWidth();
	
	public int compare(CellData<T> c1, CellData<T> c2);
	
}
