package de.timedout.oc.browser.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import de.timedout.oc.parts.FileHelper;

public class PathElement extends AbstractElement {

	final protected Path path;

	public PathElement(Path path) {
		this.path = path;
	}

	public Long getFilesize() {
		try {
			if (isDirectory()) {
				return 0l;
			}

			return Files.size(path);
		} catch (IOException e) {
			return 0l;
		}
	}

	public Path getPath() {
		return path;
	}

	public String getFilename() {
		if(path.getParent() != null){
			return "" + path.getFileName();
		} else {
			return path.toString();
		}
	}

	@Override
	public boolean isDirectory() {
		return Files.isDirectory(path);
	}

	@Override
	public Image getIcon() {
		Image image = FileHelper.getImage(path.toFile(), Display.getCurrent());
		return image;
	}

	@Override
	public boolean isReadable() {
		return Files.isReadable(path);
	}

	@Override
	public boolean isHidden() {
		try {
			return Files.isHidden(path);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		PathElement other = (PathElement) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public Element getParent() {
		Path parent = path.getParent();
		if(parent == null){
			return new SystemElement();
		} else {
			return new PathElement(parent);
		}
	}

	@Override
	public String toString() {
		return path.toString();
	}
	
	

}
