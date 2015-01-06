package de.timedout.oc.browser.model;

import java.io.File;
import java.nio.file.Path;

public class SystemListManager extends AbstractLocationMediator<SystemElement> implements LocationMediator {

	@Override
	protected void populateList() {
		list.getReadWriteLock().writeLock().lock();
		list.clear();
		list.getReadWriteLock().writeLock().unlock();

		for (File root : File.listRoots()) {
			Path path = root.toPath();
			list.getReadWriteLock().writeLock().lock();
			System.out.println(path);
			System.out.println((new PathElement(path)).equals(new PathElement(path)));
			list.add(new PathElement(path));
			list.getReadWriteLock().writeLock().unlock();
		}
	}

	@Override
	protected void keepListUpdated() {
		
	}

}
