package com.mgmtp.a12.melies.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;

/**
 * Fixes the problem described here:
 * <ul>
 * <li><a
 * href="http://techblog.ralph-schuster.eu/2013/10/13/eclipsee4-problem-with-key-bindings/comment-page-1/">Eclipse/E4:
 * Problem with Key Bindings</a></li>
 * <li><a href="http://www.eclipse.org/forums/index.php/t/550175/">Kepler SR 1 (4.3.1): e4 key bindings will be removed
 * by BindingToModelProcessor</a></li>
 * <li>Here's the link to the culprit: <a href=
 * "http://grepcode.com/file/repository.grepcode.com/java/eclipse.org/4.4.0/org.eclipse.ui/workbench/3.106.0/org/eclipse/ui/internal/BindingToModelProcessor.java#BindingToModelProcessor.removeBindings%28%29"
 * >org.eclipse.ui.internal.BindingToModelProcessor removeBindings()</a>.
 * </ul>
 * <p>
 * Solution: This ModelProcessor (when run before org.eclipse.ui.internal.BindingToModelProcessor) will add the tag
 * "type:user" to all key bindings.
 * </p>
 * <p>
 * You might adapt this logic to your needs, e.g. only add the tag to special key bindings etc.
 * </p>
 * <p>
 * Note: To ensure proper operation, ensure it is run before org.eclipse.ui.internal.BindingToModelProcessor by an
 * extension point setting in plugin.xml like this:
 * 
 * <pre>
 * &lt;extension id="keybindingfix" point="org.eclipse.e4.workbench.model"&gt;
 *   &lt;processor
 *      beforefragment="true"
 *      class="com.mgmtp.a12.melies.app.BindingContextFixModelProcessor"&gt;
 *   &lt;/processor&gt;
 *   &lt;!-- beforefragment="true" : this is important to ensure that it's run before org.eclipse.ui.internal.BindingToModelProcessor --&gt;
 * &lt;/extension&gt;
 * </pre>
 * 
 * </p>
 * 
 * @author mbackschat
 *
 */
public class BindingContextFixModelProcessor {

	@Execute
	public void execute(final MApplication app) {
		// Collect key bindings
		Set<MKeyBinding> keys = new HashSet<MKeyBinding>();
		Map<String, MBindingTable> tables = new HashMap<String, MBindingTable>();
		for (MBindingTable table : app.getBindingTables()) {
			tables.put(table.getBindingContext().getElementId(), table);
			keys.addAll(table.getBindings());
		}

		System.out.println("BindingContextFixModelProcessor ... adding tag \"type:user\" to key bindings");
		// Add "type:user" tag to key bindings
		for (MKeyBinding key : keys) {
			if (!key.getTags().contains("type:user")) {
				key.getTags().add("type:user");
				System.out.println("  ... added to key binding "
						+ key.getKeySequence() + " (ID " + key.getElementId() + ")");
			}
		}
	}
}