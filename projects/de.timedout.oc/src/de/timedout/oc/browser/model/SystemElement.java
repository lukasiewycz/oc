package de.timedout.oc.browser.model;

import org.eclipse.swt.graphics.Image;

public class SystemElement extends AbstractElement {
	
	final int hc = 260182;

	@Override
	public Long getFilesize() {
		return 0l;
	}

	@Override
	public String getFilename() {
		return "Root";
	}

	@Override
	public boolean isDirectory() {
		return true;
	}

	@Override
	public Image getIcon() {
		return null;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public Element getParent() {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hc;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemElement other = (SystemElement) obj;
		if (hc != other.hc)
			return false;
		return true;
	}
	
	

}
