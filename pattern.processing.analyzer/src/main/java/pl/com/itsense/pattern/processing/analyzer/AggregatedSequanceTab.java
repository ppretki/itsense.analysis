package pl.com.itsense.pattern.processing.analyzer;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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
public class AggregatedSequanceTab extends VerticalLayout 
{
	/** */
	private final String TABLE_COLUMN_ID = "Id";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_NAME = "Name";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_DURATION = "Duration";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_DURATION_TOTAL = "Duration total";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_COUNT = "Count";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_DURATION_AVG = "Duration AVG";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_DURATION_STD = "Duration STD";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_DURATION_MIN = "Duration MIN";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_DURATION_MAX = "Duration MAX";
	/** */
	private final String sequenceId;
	/** */
	private Table aggregateTable;
	/** */
	private Table sequenceTable;
	/** */
	private String selectedSequenceName;
	/** */
	private final SessionFactory sessionFactory;

	
	/**
	 * 
	 */
	public AggregatedSequanceTab(final String sequenceId, final SessionFactory sessionFactory) 
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
		aggregateTable = new Table("Aggragations");
		aggregateTable.setHeight(50, Unit.PERCENTAGE);
		aggregateTable.setWidth(100, Unit.PERCENTAGE);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_NAME, String.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_COUNT, Long.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_DURATION_TOTAL, Long.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_DURATION_AVG, Double.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_DURATION_MIN, Long.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_DURATION_MAX, Long.class, null);

		aggregateTable.setSelectable(true);
		aggregateTable.setImmediate(true);
		aggregateTable.addValueChangeListener(new Property.ValueChangeListener() 
		{
		    /** */
			public void valueChange(final ValueChangeEvent event) 
		    {
				selectedSequenceName = (String) aggregateTable.getValue();
				fillSequanceTable();
		    }
		});
		fillAggregateTable();
		addComponent(aggregateTable);
		
		sequenceTable = new Table("Sequances");
		sequenceTable.setHeight(50, Unit.PERCENTAGE);
		sequenceTable.setWidth(100, Unit.PERCENTAGE);
		sequenceTable.addContainerProperty(TABLE_COLUMN_ID, Long.class, null);
		sequenceTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_NAME, String.class, null);
		sequenceTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_DURATION, Long.class, null);

		fillSequanceTable();
		addComponent(sequenceTable);

	}


	/**
	 * 
	 */
	private void fillAggregateTable()
	{
		aggregateTable.removeAllItems();
		Session session = null;
		Transaction trx = null;
		try
		{
			session = sessionFactory.openSession();
			trx = session.beginTransaction();
			final Criteria c = session.createCriteria(SequenceDB.class)
					.add(Restrictions.eq("sequenceId", sequenceId))
					.setProjection(
							Projections.projectionList().
							add(Projections.groupProperty("name")).
							add(Projections.rowCount()).
							add(Projections.sum("duration")).
							add(Projections.avg("duration")).
							add(Projections.min("duration")).
							add(Projections.max("duration"))
					);
			
			for (final Iterator<Object[]> iterator = c.list().iterator(); iterator.hasNext();)
			{
				final Object[] row = iterator.next();
				aggregateTable.addItem(row, row[0]);	
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
	private void fillSequanceTable()
	{
		sequenceTable.removeAllItems();
		if (selectedSequenceName != null)
		{
			Session session = null;
			Transaction trx = null;
			try
			{
				session = sessionFactory.openSession();
				trx = session.beginTransaction();
				final Criteria c = session.createCriteria(SequenceDB.class).add(Restrictions.eqOrIsNull("name", selectedSequenceName));
				if (c != null)
				{
					for (final Iterator<SequenceDB> iterator = c.list().iterator(); iterator.hasNext();)
					{
						final SequenceDB sequenceDB = iterator.next();
						sequenceTable.addItem(getTableRow(sequenceDB), sequenceDB.getId());	
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
		row[0] = sequenceDB.getId();
		row[1] = sequenceDB.getName();
		row[2] = sequenceDB.getDuration();
		return row;
	}
}
