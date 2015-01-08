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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.SortedList;
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
import de.timedout.oc.browser.model.ParentElement;
import de.timedout.oc.browser.model.PathElement;
import de.timedout.oc.browser.model.PathListManager;
import de.timedout.oc.browser.model.SystemElement;
import de.timedout.oc.browser.model.SystemListManager;

public class BrowserPart {

	@Inject
	private MDirtyable dirty;

	private Text label;

	private Table table;

	private Element location = null;

	private LocationMediator locationManager = null;

	private ObservableElementList<Element> observableList;

	private SortedList<Element> sortedList;
	
	private SortedList<Element> groupingList;

	private DefaultEventTableViewer<Element> tableViewer;

	private Composite parent;

	@PostConstruct
	public void createComposite(Composite parent) {
		this.parent = parent;

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		parent.setLayout(layout);

		label = new Text(parent, SWT.NONE);
		label.setText("Hallo");
		label.setEditable(false);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

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

		groupingList = new SortedList<Element>(sortedList, new AdvancedFileTypeComparator());

		tableViewer = GlazedListsSWT.eventTableViewerWithThreadProxyList(groupingList, table, new ColumnTableFormat(columnList));
		tableViewer.setTableItemConfigurer(new CustomTableItemConfigurer());

		TableComparatorChooser<Element> tcc = TableComparatorChooser.install(tableViewer, sortedList, false);

		table.setHeaderVisible(true);
		// table.setLinesVisible(true);

		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				
			}
		});

		/*
		 * table.addPaintListener(new PaintListener() {
		 * 
		 * @Override public void paintControl(PaintEvent e) {
		 * e.gc.drawFocus(e.x, e.y, e.width, e.height);
		 * 
		 * } });
		 */

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

		/*
		 * EventList<Element> list = tableViewer.getSelected();
		 * 
		 * list.addListEventListener(new ListEventListener<Element>() {
		 * 
		 * @Override public void listChanged(ListEvent<Element> event) {
		 * if(!list.isEmpty()){ Element element = list.get(0);
		 * selectionCache.put(location, element); } } });
		 */

		for (Method method : table.getClass().getDeclaredMethods()) {
			if (method.toString().toLowerCase().contains("focus")) {
				System.out.println(method);
			}
		}

		try {
			setTableFocusMethod = table.getClass().getDeclaredMethod("setFocusIndex", Integer.TYPE);
			setTableFocusMethod.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		tcc.addSortListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println(arg0);
				observableList.getReadWriteLock().writeLock().lock();
				try {
					EventList<Element> list = tableViewer.getSelected();
					if(list.size()>0){
						Element e = list.iterator().next();
						System.out.println(groupingList.indexOf(e));
						setTableFocusMethod.invoke(table, groupingList.indexOf(e));
						table.showItem(table.getItem(groupingList.indexOf(e)));
						table.setFocus();
						table.redraw();
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				observableList.getReadWriteLock().writeLock().unlock();
				
			}
		});
	}

	Method setTableFocusMethod = null;

	Map<Element, Element> selectionCache = new HashMap<Element, Element>();

	public synchronized void setLocation(Element location) {
		if (location == null) {
			return;
		}

		if (this.location != null && location.equals(this.location.getParent())) { // parent
																					// navigation
			System.out.println(this.location + " " + location);
			selectionCache.put(location, this.location);
		}

		if (locationManager != null) {
			locationManager.disconnect();
		}

		if (location instanceof PathElement) {
			locationManager = new PathListManager();
		} else if (location instanceof SystemElement) {
			locationManager = new SystemListManager();
		} else if (location instanceof ParentElement) {
			setLocation(this.location.getParent());
			return;
		}

		this.location = location;

		label.setText(location.toString());

		locationManager.init(observableList, location);

		locationManager.addPopulateListener(new PopulateListener() {
			@Override
			public void finished() {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						observableList.getReadWriteLock().writeLock().lock();
						// table.select(1);
						table.setFocus();

						EventList<Element> list = tableViewer.getTogglingSelected();

						Element selectionItem = selectionCache.get(location);
						try {
							if (selectionItem != null && observableList.contains(selectionItem)) {
								list.add(selectionItem);
								try {
									setTableFocusMethod.invoke(table, groupingList.indexOf(selectionItem));
									table.redraw();
								} catch (IllegalAccessException | InvocationTargetException e) {
									e.printStackTrace();
								}
							} else {
								list.add(sortedList.get(0));
							}
						} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
						}

						/*
						 * System.out.println(table.getSelectionIndex()+" "+table
						 * .getItem(table.getSelectionIndex()));
						 * table.showItem(table
						 * .getItem(table.getSelectionIndex()));
						 * 
						 * System.out.println(observableList.get(1));
						 */
						observableList.getReadWriteLock().writeLock().unlock();
					}
				});

			}
		});

		locationManager.connect();
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

	public Element getCursorElement() {
		EventList<Element> selected = tableViewer.getSelected();

		if (selected.size() == 1) {
			return selected.get(0);
		}

		return null;
	}
}