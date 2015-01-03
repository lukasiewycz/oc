package de.timedout.oc.browser.columns;

import java.text.DecimalFormat;

import org.eclipse.swt.graphics.Image;

import de.timedout.oc.browser.cells.CellData;
import de.timedout.oc.browser.model.Element;

public class FilesizeColumn extends AbstractBrowserColumn<Long> {

	final String[] units = new String[] { "", "K", "M", "G", "T" };
	
	@Override
	public CellData<Long> getCell(final Element file) {
		return new CellData<Long>() {
			@Override
			public Long getContent() {
				return (file.isDirectory() ? 0l : file.getFilesize());
			}

			@Override
			public Image getIcon() {
				return null;
			}

			@Override
			public String getText() {
				long size = getContent();
				if(file.isDirectory()){
					return "<DIR>";
				}
				
			    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
			    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
			}

			@Override
			public Element getFile() {
				return file;
			}
		};
	}

	@Override
	public String getName() {
		return "Size";
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	public int compare(CellData<Long> c1, CellData<Long> c2) {
		Long l1 = c1.getContent();
		Long l2 = c2.getContent();
		return l1.compareTo(l2);
	}


}
