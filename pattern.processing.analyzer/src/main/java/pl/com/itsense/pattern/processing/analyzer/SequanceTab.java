package pl.com.itsense.pattern.processing.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import pl.com.itsense.analysis.event.db.SequenceDB;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Link;
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
public class SequanceTab extends VerticalLayout 
{
	/** */
	private final String TABLE_COLUMN_SEQUANCE_ID = "Id";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_NAME = "Name";
	/** */
	private final String sequenceId;
	/** */
	private Table table;
	/** */
	private final SessionFactory sessionFactory;

	
	/**
	 * 
	 */
	public SequanceTab(final String sequenceId, final SessionFactory sessionFactory) 
	{
		this.sequenceId = sequenceId;
		this.sessionFactory = sessionFactory;
		buildUI();
	}

	/**
	 *
	 */
	private void buildUI() 
	{
		table = new Table();
		table.setSizeFull();
		table.addContainerProperty(TABLE_COLUMN_SEQUANCE_ID, String.class, null);
		table.addContainerProperty(TABLE_COLUMN_SEQUANCE_NAME, String.class, null);
		fillTable();
		addComponent(table);
	}


	/**
	 * 
	 */
	private void fillTable()
	{
		table.removeAllItems();
		Session session = null;
		Transaction trx = null;
		try
		{
			session = sessionFactory.openSession();
			trx = session.beginTransaction();
			final Criteria c = session.createCriteria(SequenceDB.class).add(Restrictions.eq("sequenceId", sequenceId));
			for (final Iterator iterator = c.list().iterator(); iterator.hasNext();)
			{
				final SequenceDB sequenceDB = (SequenceDB) iterator.next();
				table.addItem(getTableRow(sequenceDB), sequenceDB.getId());	
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
	}
	/**
	 * 
	 * @return
	 */
	private Object[] getTableRow(final SequenceDB sequenceDB)
	{
		
		final Object[] row = new Object[2];
		row[0] = String.valueOf(sequenceDB.getId());
		row[1] = sequenceDB.getName();
		return row;
	}
	

}
