package pl.com.itsense.pattern.processing.analyzer;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
/**
 * 
 * @author ppretki
 *
 */
public class WorkspaceView extends CustomComponent implements View 
{
	/** */
	private VerticalLayout mainLayout;
	/** */
	private MenuBar menuBar;
	/** */
	private MenuItem browseMenuItem;
	/** */
	private final Navigator navigator;
	
	/**
	 * 
	 */
	public WorkspaceView(final Navigator navigator) 
	{
		this.navigator = navigator;
	}
	/**
	 * 
	 */
	public void enter(final ViewChangeEvent event) 
	{
		setCompositionRoot(buildUI());
	}

	/**
	 *
	 */
	private VerticalLayout buildUI() 
	{
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.addComponent(buildMenuBar());
		return mainLayout;
	}
	/**
	 * 
	 * @return
	 */
	private MenuBar buildMenuBar()
	{
		menuBar = new MenuBar();
		browseMenuItem = menuBar.addItem("Browse Results", new Command() 
		{
			/** */
			private static final long serialVersionUID = 1L;
			/**
			 * 
			 */
			public void menuSelected(final MenuItem selectedItem) 
			{
				navigator
				
			}
		});
		
		return menuBar; 
	}


}
