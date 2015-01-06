package de.timedout.oc.browser.model;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.odell.glazedlists.EventList;

public class PathListManager extends AbstractLocationMediator<PathElement> implements LocationMediator {

	final Map<Path, PathElement> map = new HashMap<Path, PathElement>();
	
	@Override
	public synchronized void init(EventList<Element> list, Element element) {
		if (element == null || !element.isDirectory()) {
			throw new IllegalArgumentException("PathElement is not a directory");
		}
		super.init(list, element);
	}

	@Override
	protected void populateList() {
		final Path dir = location.getPath();
		
		list.getReadWriteLock().writeLock().lock();
		list.clear();
		list.add(new ParentElement());
		list.getReadWriteLock().writeLock().unlock();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
			List<PathElement> temp = new ArrayList<PathElement>();
			
			for (Path path : directoryStream) {
				temp.add(create(path, map));
				
				if(temp.size()>1000){
					list.getReadWriteLock().writeLock().lock();
					list.addAll(temp);
					list.getReadWriteLock().writeLock().unlock();
					temp.clear();
				}
				if (!isConnected) {
					break;
				}
			}
			
			if(temp.size()>0){
				list.getReadWriteLock().writeLock().lock();
				list.addAll(temp);
				list.getReadWriteLock().writeLock().unlock();
				temp.clear();
			}
			
			
		} catch (IOException ex) {
		}

		
		
	}
	
	@Override
	protected void keepListUpdated() {
		final Path dir = location.getPath();
		
		try (WatchService watcher = dir.getFileSystem().newWatchService()) {
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			while (isConnected) {
				WatchKey key = watcher.take();

				for (WatchEvent<?> e : key.pollEvents()) {
					Path path = dir.resolve((Path) e.context());

					if (e.kind() == ENTRY_CREATE) {
						if (!map.containsKey(path)) {
							list.getReadWriteLock().writeLock().lock();
							list.add(create(path, map));
							list.getReadWriteLock().writeLock().unlock();
						}
					}
					if (e.kind() == ENTRY_DELETE) {
						if (map.containsKey(path)) {
							list.getReadWriteLock().writeLock().lock();
							list.remove(create(path, map));
							list.getReadWriteLock().writeLock().unlock();
							map.remove(path);
						}
					}
					if (e.kind() == ENTRY_MODIFY) {
						if (map.containsKey(path)) {
							list.getReadWriteLock().writeLock().lock();
							map.get(path).fireModify();
							list.getReadWriteLock().writeLock().unlock();
						}
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (InterruptedException | IOException e1) {
		}
		
	}
	
	private PathElement create(Path path, Map<Path, PathElement> map) {
		if (map.containsKey(path)) {
			return map.get(path);
		} else {
			PathElement pathElement = new PathElement(path);
			map.put(path, pathElement);
			return pathElement;
		}
	}




}
