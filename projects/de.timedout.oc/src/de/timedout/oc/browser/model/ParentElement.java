package de.timedout.oc.browser.model;

import org.eclipse.swt.graphics.Image;

public class ParentElement extends AbstractElement {

	@Override
	public Long getFilesize() {
		return 0l;
	}

	@Override
	public String getFilename() {
		return "..";
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

}
