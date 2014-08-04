package pl.com.itsense.pattern.processing.analyzer;

import java.util.Date;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pl.com.itsense.analysis.event.db.EventDB;
import pl.com.itsense.analysis.event.db.SequenceDB;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author ppretki
 *
 */
public class SequanceTab extends VerticalLayout 
{
	/** */
	private final String TABLE_COLUMN_ID = "Id";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_NAME = "Name";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_DURATION = "Duration";
	/** */
	private final String TABLE_COLUMN_TERM_ID = "Event ID";
	/** */
	private final String TABLE_COLUMN_TERM_LINE = "Line";
	/** */
	private final String TABLE_COLUMN_TERM_TIMESTAMP = "Timestamp";

	/** */
	private final String sequenceId;
	/** */
	private Table sequenceTable;
	/** */
	private Table termTable;
	/** */
	private long selectedSequenceId = -1;

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
		sequenceTable = new Table("Sequance");
		sequenceTable.setHeight(50, Unit.PERCENTAGE);
		sequenceTable.setWidth(100, Unit.PERCENTAGE);
		sequenceTable.addContainerProperty(TABLE_COLUMN_ID, String.class, null);
		sequenceTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_NAME, String.class, null);
		sequenceTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_DURATION, Long.class, null);
		sequenceTable.setSelectable(true);
		sequenceTable.setImmediate(true);
		sequenceTable.addValueChangeListener(new Property.ValueChangeListener() 
		{
		    /** */
			public void valueChange(final ValueChangeEvent event) 
		    {
				selectedSequenceId = (Long) sequenceTable.getValue();
				fillTermTable();
		    }
		});
		fillSequanceTable();
		addComponent(sequenceTable);
		
		termTable = new Table("Events");
		termTable.setHeight(50, Unit.PERCENTAGE);
		termTable.setWidth(100, Unit.PERCENTAGE);
		termTable.addContainerProperty(TABLE_COLUMN_ID, String.class, null);
		termTable.addContainerProperty(TABLE_COLUMN_TERM_ID, String.class, null);
		termTable.addContainerProperty(TABLE_COLUMN_TERM_TIMESTAMP, Date.class, null);
		termTable.addContainerProperty(TABLE_COLUMN_TERM_LINE, String.class, null);
		fillTermTable();
		addComponent(termTable);

	}


	/**
	 * 
	 */
	private void fillSequanceTable()
	{
		sequenceTable.removeAllItems();
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
				sequenceTable.addItem(getTableRow(sequenceDB), sequenceDB.getId());	
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
	 */
	private void fillTermTable()
	{
		System.out.println("selctedSeq = " + selectedSequenceId);
		termTable.removeAllItems();
		if (selectedSequenceId > -1)
		{
			Session session = null;
			Transaction trx = null;
			try
			{
				session = sessionFactory.openSession();
				trx = session.beginTransaction();
				final SequenceDB sequenceDB = (SequenceDB)session.get(SequenceDB.class, selectedSequenceId);
				if (sequenceDB != null)
				{
					for (final EventDB eventDB : sequenceDB.getEvents())
					{
						termTable.addItem(getTableRow(eventDB), eventDB.getId());	
					}
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
	}

	/**
	 * 
	 * @return
	 */
	private Object[] getTableRow(final SequenceDB sequenceDB)
	{
		
		final Object[] row = new Object[3];
		row[0] = String.valueOf(sequenceDB.getId());
		row[1] = sequenceDB.getName();
		row[2] = sequenceDB.getDuration();
		return row;
	}
	
	/**
	 * 
	 * @return
	 */
	private Object[] getTableRow(final EventDB eventDB)
	{
		
		final Object[] row = new Object[4];
		row[0] = String.valueOf(eventDB.getId());
		row[1] = eventDB.getEventId();
		row[2] = eventDB.getTimestamp();
		row[3] = eventDB.getLine();
		return row;
	}

}
