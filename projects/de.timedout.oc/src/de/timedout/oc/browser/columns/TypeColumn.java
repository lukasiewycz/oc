package de.timedout.oc.browser.columns;

import org.eclipse.swt.graphics.Image;

import de.timedout.oc.browser.cells.CellData;
import de.timedout.oc.browser.model.Element;
import de.timedout.oc.browser.model.ParentElement;

public class TypeColumn extends AbstractBrowserColumn<Integer> {

	@Override
	public CellData<Integer> getCell(Element file) {
		return new CellData<Integer>(){
			@Override
			public Integer getContent() {
				return getValue(file);
			}

			@Override
			public Image getIcon() {
				return null;
			}

			@Override
			public String getText() {
				return ""+getContent();
			}

			@Override
			public Element getFile() {
				return file;
			}
		};
	}

	@Override
	public String getName() {
		return "Type";
	}

	@Override
	public int getWidth() {
		return 100;
	}
	
	protected Integer getValue(Element file){
		if(file instanceof ParentElement){
			return 0;
		} else if(file.isDirectory()){
			return 1;
		} else {
			return 2;
		}
	}

	@Override
	public int compare(CellData<Integer> c1, CellData<Integer> c2) {
		Integer s1 = c1.getContent();
		Integer s2 = c2.getContent();
		return s1.compareTo(s2);
	}

}
