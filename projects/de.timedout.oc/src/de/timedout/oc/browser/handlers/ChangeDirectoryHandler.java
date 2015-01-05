 
package de.timedout.oc.browser.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

import de.timedout.oc.browser.BrowserPart;
import de.timedout.oc.browser.model.Element;

public class ChangeDirectoryHandler {
	
	@Execute
	public void execute(Shell shell, @Active MPart part) {
		
		if(part.getObject() instanceof BrowserPart){
			BrowserPart browserPart = (BrowserPart)part.getObject();
			Element element = browserPart.getCursorElement();
			
			if( element != null && element.isDirectory()){
				browserPart.setLocation(element);
			}
		}
	}
	
	@CanExecute
	public boolean canExecute(Shell shell, @Active MPart part){
		if(part.getObject() instanceof BrowserPart){
			BrowserPart browserPart = (BrowserPart)part.getObject();
			Element element = browserPart.getCursorElement();
			
			return element != null && element.isDirectory();
		}
		return false;
		
	}
		
}