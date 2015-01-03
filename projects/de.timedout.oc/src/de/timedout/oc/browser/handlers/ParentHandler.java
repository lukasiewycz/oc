 
package de.timedout.oc.browser.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

public class ParentHandler {
	
	@Execute
	public void execute(Shell shell) {
		System.out.println("Parent");
	}
	
	@CanExecute
	public boolean canExecute(Shell shell){
		return true;
	}
		
}