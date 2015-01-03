package de.timedout.oc.browser.columns;

import java.util.Comparator;

import de.timedout.oc.browser.cells.CellData;

public abstract class AbstractBrowserColumn<T> implements BrowserColumn<T> {

	@Override
	public Comparator<CellData<T>> getComparator() {
		Comparator<CellData<T>> comparator = new Comparator<CellData<T>>() {
			@Override
			public int compare(CellData<T> o1, CellData<T> o2) {
				return AbstractBrowserColumn.this.compare(o1, o2);
			}
		};
		return comparator;
	}

	public static int BASE = 0;
	public static int EXTENSION = 1;

	protected String[] splitBasenameExtension(String filename) {
		String[] parts = (filename).split("\\.(?=[^\\.]+$)");

		if (parts.length == 1 || (parts[BASE].length() == 0 && parts[EXTENSION].length() > 0)) {
			return new String[] { filename, "" };
		} else {
			return parts;
		}
	}

}
