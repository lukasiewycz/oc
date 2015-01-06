package de.timedout.oc.browser.model;

import java.util.EventListener;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import ca.odell.glazedlists.EventList;

public abstract class AbstractLocationMediator<E extends Element> implements LocationMediator {

	protected E location;
	protected EventList<Element> list;
	protected Thread thread = new Thread(){
		public void run(){
			populateList();
			for(PopulateListener listener: listeners){
				listener.finished();
			}
			keepListUpdated();
		}
	};

	protected boolean isInit = false;
	protected boolean isConnected = false;
	
	protected Set<PopulateListener> listeners = new CopyOnWriteArraySet<PopulateListener>();
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void init(EventList<Element> list, Element location) {
		if (!isInit) {
			this.location = (E)location;
			this.list = list;
			isInit = true;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public synchronized void connect() {
		if (!isInit) {
			throw new IllegalArgumentException("Mediator has to be initialized");
		}

		if (!isConnected) {
			thread.start();
			isConnected = true;
		}

	}

	@Override
	public synchronized void disconnect() {
		if (isConnected) {
			isConnected = false;

			try {
				thread.interrupt();
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void populateList();
	
	protected abstract void keepListUpdated();

	@Override
	public void addPopulateListener(PopulateListener listener) {
		listeners.add(listener);
	}
	
	

}
