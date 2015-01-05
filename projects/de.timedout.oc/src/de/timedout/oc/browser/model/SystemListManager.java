package de.timedout.oc.browser.model;

import java.io.File;
import java.nio.file.Path;

import ca.odell.glazedlists.EventList;

public class SystemListManager implements ListManager {

	protected Thread thread;
	protected EventList<Element> list;

	@Override
	public void init(EventList<Element> list, Element location) {
		SystemElement system = (SystemElement) location;
		
		this.list = list;
	}

	@Override
	public void connect() {
		thread = new Thread() {
			public void run(){
				list.getReadWriteLock().writeLock().lock();
				list.clear();
				list.getReadWriteLock().writeLock().unlock();

				for (File root : File.listRoots()) {
					Path path = root.toPath();
					list.getReadWriteLock().writeLock().lock();
					list.add(new PathElement(path));
					list.getReadWriteLock().writeLock().unlock();
				}
			}
		};
		thread.start();
	}

	@Override
	public void disconnect() {
		try {
			thread.interrupt();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
