package de.timedout.oc.browser.cells;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;

import ca.odell.glazedlists.swt.TableItemConfigurer;
import de.timedout.oc.browser.model.Element;

public class CustomTableItemConfigurer implements TableItemConfigurer<Element> {
	
	@Override
	public void configure(TableItem tableItem, Element item, Object obj, int row, int column) {
		CellData<?> cellData = (CellData<?>) obj;
		tableItem.setText(column, cellData.getText());
		Image icon = cellData.getIcon();
		if (icon != null) {
			tableItem.setImage(column, icon);
		}
		// tableItem.setForeground(0,
		// Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	}

}
