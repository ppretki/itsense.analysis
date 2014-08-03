package pl.com.itsense.pattern.processing.analyzer;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import pl.com.itsense.ecommerce.datamodel.impl.Product;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
/**
 * 
 * @author ppretki
 *
 */
public class SequanceView extends CustomComponent implements View 
{
	/** */
	private VerticalLayout mainLayout;
	/** */
	private MenuBar menuBar;
	/** */
	private MenuItem browseMenuItem;
	/** */
	private final String sequanceId;
	/** */
	private Table table;

	
	/**
	 * 
	 */
	public SequanceView(final String sequanceId) 
	{
		this.sequanceId = sequanceId;
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
		table = new Table();
		table.setSizeFull();
		table.addContainerProperty(TABLE_COLUMN_MATERIAL_ID, String.class, null);
		table.addContainerProperty(TABLE_COLUMN_PRODUCT_ID, Long.class, null);
		table.addContainerProperty(TABLE_COLUMN_PRODUCT_NAME, String.class, null);
		table.addContainerProperty(TABLE_COLUMN_ID_ACTIONS, Component.class, null);
		table.addContainerProperty(TABLE_COLUMN_LINKS, Component.class, null);
		fillTable();
		setFirstComponent(table);
		return mainLayout;
	}


	/**
	 * 
	 */
	private void fillTable()
	{
		System.out.println("VideoCatalogView.fillTable() +++");
		table.removeAllItems();
		Session session = null;
		Transaction trx = null;
		try
		{
			session = webshopManager.getSessionFactory().openSession();
			trx = session.beginTransaction();
			final Criteria c = session.createCriteria(Product.class)
					           .addOrder(Order.asc("connectorId"))
					           .setFetchMode("availability", FetchMode.SELECT)
					           .setFetchMode("description", FetchMode.SELECT)
					           .setFetchMode("variantProducts", FetchMode.SELECT)
					           .setFetchMode("properties", FetchMode.SELECT)
					           .setFetchMode("attributes", FetchMode.SELECT)
					           .setFetchMode("currency", FetchMode.SELECT)
					           .setFetchMode("productLinks", FetchMode.JOIN);
			for (final Iterator iterator = c.list().iterator(); iterator.hasNext();)
			{
				final Product product = (Product) iterator.next();
				table.addItem(getTableRow(product), product.getId());	
			}
			trx.commit();
			
		}
		catch(HibernateException e)
		{
			System.out.println(e.getMessage());
			Thread.dumpStack();
			if (trx != null)
			{
				trx.rollback();
			}
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		if (selectedProductId >= 0)
		{
			System.out.println("selectedMaterial = " + selectedProductId);
			table.setCurrentPageFirstItemId(selectedProductId);
			selectedProductId = -1;
		}
		System.out.println("VideoCatalogView.fillTable() ---");
	}
	

}
