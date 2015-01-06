/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package de.timedout.oc.browser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.swt.DefaultEventTableViewer;
import ca.odell.glazedlists.swt.GlazedListsSWT;
import ca.odell.glazedlists.swt.TableComparatorChooser;
import de.timedout.oc.browser.cells.CustomTableItemConfigurer;
import de.timedout.oc.browser.columns.AdvancedFileTypeComparator;
import de.timedout.oc.browser.columns.BasenameColumn;
import de.timedout.oc.browser.columns.BrowserColumn;
import de.timedout.oc.browser.columns.ColumnTableFormat;
import de.timedout.oc.browser.columns.ExtensionColumn;
import de.timedout.oc.browser.columns.FilesizeColumn;
import de.timedout.oc.browser.model.Element;
import de.timedout.oc.browser.model.LocationMediator;
import de.timedout.oc.browser.model.LocationMediator.PopulateListener;
import de.timedout.oc.browser.model.PathElement;
import de.timedout.oc.browser.model.PathListManager;
import de.timedout.oc.browser.model.SystemElement;
import de.timedout.oc.browser.model.SystemListManager;

public class BrowserPart {

	@Inject
	private MDirtyable dirty;

	private Table table;

	private Element location = null;

	private LocationMediator locationManager = null;

	private ObservableElementList<Element> observableList;
	
	private SortedList<Element> sortedList;

	private DefaultEventTableViewer<Element> tableViewer;

	@PostConstruct
	public void createComposite(Composite parent) {
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		parent.setLayout(layout);

		List<BrowserColumn<?>> columnList = new ArrayList<BrowserColumn<?>>();
		// columnList.add(new TypeColumn());
		columnList.add(new BasenameColumn());
		columnList.add(new ExtensionColumn());
		columnList.add(new FilesizeColumn());

		EventList<Element> eventList = GlazedLists.eventList(new ArrayList<Element>());

		ObservableElementList.Connector<Element> beanConnector = GlazedLists.beanConnector(Element.class);
		observableList = new ObservableElementList<Element>(eventList, beanConnector);

		FilterList<Element> filterList = new FilterList<Element>(observableList, new Matcher<Element>() {
			@Override
			public boolean matches(Element element) {
				return !element.isHidden() && element.isReadable();
			}
		});

		sortedList = new SortedList<Element>(filterList, new AdvancedFileTypeComparator());

		table = new Table(parent, SWT.NONE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.SINGLE);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		SortedList<Element> groupingList = new SortedList<Element>(sortedList, new AdvancedFileTypeComparator());

		tableViewer = GlazedListsSWT.eventTableViewerWithThreadProxyList(groupingList, table, new ColumnTableFormat(columnList));
		tableViewer.setTableItemConfigurer(new CustomTableItemConfigurer());
		

		// TableComparatorChooser<Element> tcc =
		TableComparatorChooser.install(tableViewer, sortedList, false);
		
		

		table.setHeaderVisible(true);
		// table.setLinesVisible(true);

		Path directory = Paths.get(System.getProperty("user.home"));
		setLocation(new PathElement(directory));

		/*
		 * Thread t = new Thread(){ public void run(){ try {
		 * Thread.sleep(50000); } catch (InterruptedException e) {
		 * e.printStackTrace(); } pathListManager.disconnect(); } }; t.start();
		 */

		/*
		 * table.addKeyListener(new KeyListener() {
		 * 
		 * @Override public void keyReleased(org.eclipse.swt.events.KeyEvent e)
		 * { System.out.println("release "+e); }
		 * 
		 * @Override public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
		 * System.out.println("pressed "+e); } });
		 */
		
		
		/*EventList<Element> list = tableViewer.getSelected();
		
		list.addListEventListener(new ListEventListener<Element>() {
			@Override
			public void listChanged(ListEvent<Element> event) {
				if(!list.isEmpty()){
					Element element = list.get(0);
					selectionCache.put(location, element);
				}
			}	
		});*/
		
	}
	
	Map<Element,Element> selectionCache = new HashMap<Element,Element>();

	public void setLocation(Element location) {
		if (this.location != null){
			System.out.println(this.location+" "+location);
		}
		
		if (this.location != null && location.equals(this.location.getParent())){ // parent navigation
			System.out.println(this.location+" "+location);
			selectionCache.put(location, this.location);
		}
		
		if (locationManager != null) {
			locationManager.disconnect();
		}

		this.location = location;
		
		LocationMediator manager = null;
		if(location instanceof PathElement){
			manager = new PathListManager();
		} else if(location instanceof SystemElement){
			manager = new SystemListManager();
		} else {
			return;
		}
		
		manager.init(observableList, location);
		
		manager.addPopulateListener(new PopulateListener() {
			@Override
			public void finished() {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						observableList.getReadWriteLock().writeLock().lock();
						//table.select(1);
						table.setFocus();
						
						EventList<Element> list = tableViewer.getTogglingSelected();
						
						Element selectionItem = selectionCache.get(location);
						if(selectionItem != null && observableList.contains(selectionItem)){
							list.add(selectionItem);
						}
						
						
						
						
						/*System.out.println(table.getSelectionIndex()+" "+table.getItem(table.getSelectionIndex()));
						table.showItem(table.getItem(table.getSelectionIndex()));
						
						System.out.println(observableList.get(1));*/
						observableList.getReadWriteLock().writeLock().unlock();
					}
				});
				
				
			}
		});
		
		
		manager.connect();
	}

	public Element getLocation() {
		return location;
	}

	@Focus
	public void setFocus() {
		table.setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	public Element getCursorElement(){
		EventList<Element> selected = tableViewer.getSelected();
		
		if(selected.size()==1){
			return selected.get(0);
		}
		
		return null;
	}
}