package de.timedout.oc.browser.columns;

import java.util.Comparator;

import de.timedout.oc.browser.model.Element;
import de.timedout.oc.browser.model.ParentElement;

public class AdvancedFileTypeComparator implements Comparator<Element> {
	
	private int getValue(Element file){
		if(file instanceof ParentElement){
			return 0;
		} else if(file.isDirectory()){
			return 1;
		} else {
			return 2;
		}
	}
	
	
	@Override
	public int compare(Element o1, Element o2) {
		return getValue(o1) - getValue(o2);
	}

}
