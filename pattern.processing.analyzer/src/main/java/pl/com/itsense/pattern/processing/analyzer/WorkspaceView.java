package pl.com.itsense.pattern.processing.analyzer;

import org.hibernate.SessionFactory;

import pl.com.itsense.pattern.processing.analyzer.query.QueryUtil;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TabSheet;
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
	private TabSheet workspace;
	/** */
	private MenuItem browseMenuItem;
	/** */
	private MenuItem allSequancesMenuItem;
	/** */
	private MenuItem aggregatedSequancesMenuItem;
	/** */
	private final Navigator navigator;
	/** */
	private final SessionFactory sessionFactory;
	
	/**
	 * 
	 */
	public WorkspaceView(final Navigator navigator, final SessionFactory sessionFactory) 
	{
		this.navigator = navigator;
		this.sessionFactory = sessionFactory;
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
		mainLayout.addComponent(buildWorkspace());
		return mainLayout;
	}
	/**
	 * 
	 * @return
	 */
	private TabSheet buildWorkspace()
	{
		workspace = new TabSheet();
		return workspace;
	}

	/**
	 * 
	 * @return
	 */
	private MenuBar buildMenuBar()
	{
		menuBar = new MenuBar();
		browseMenuItem = menuBar.addItem("Browse", null);
		allSequancesMenuItem = browseMenuItem.addItem("All", null);
		aggregatedSequancesMenuItem = browseMenuItem.addItem("Aggreated", null);

		for(final String sequence : QueryUtil.getSequenceIds(sessionFactory))
		{
			allSequancesMenuItem.addItem(sequence, new Command() 
			{
				public void menuSelected(final MenuItem selectedItem) 
				{
					workspace.addTab( new SequanceTab(sequence, sessionFactory),"Detailed View").setClosable(true);
				}
			});
			
			aggregatedSequancesMenuItem.addItem(sequence, new Command() 
			{
				public void menuSelected(final MenuItem selectedItem) 
				{
					workspace.addTab( new AggregatedSequanceTab(sequence, sessionFactory), "Aggreagted View").setClosable(true);
				}
			});
			
			
			
		}
		return menuBar; 
	}


}
