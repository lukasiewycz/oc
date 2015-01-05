 
package de.timedout.oc.browser.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

import de.timedout.oc.browser.BrowserPart;
import de.timedout.oc.browser.model.Element;

public class ParentDirectoryHandler {
	
	@Execute
	public void execute(Shell shell, @Active MPart part) {
		
		if(part.getObject() instanceof BrowserPart){
			BrowserPart browserPart = (BrowserPart)part.getObject();
			System.out.println("goto parent");
			
			Element parent = browserPart.getLocation().getParent();
			
			if(parent != null){
				browserPart.setLocation(parent);
			}
		}
	}
	
	@CanExecute
	public boolean canExecute(Shell shell, @Active MPart part){
		if(part.getObject() instanceof BrowserPart){
			BrowserPart browserPart = (BrowserPart)part.getObject();
			return (browserPart.getLocation().getParent() != null);
		}
		return false;
		
	}
		
}