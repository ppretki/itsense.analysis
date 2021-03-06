package pl.com.itsense.pattern.processing.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;

import pl.com.itsense.eventprocessing.db.SequenceDB;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.HorizontalLayout;
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
	private final String TABLE_COLUMN_SEQUANCE_NAME = "NAME";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_VALUE = "VALUE";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_TOTAL = "TOTAL";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_COUNT = "COUNT";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_AVG = "AVG";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_STD = "STD";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_MIN = "MIN";
	/** */
	private final String TABLE_COLUMN_SEQUANCE_MAX = "MAX";
	/** */
	private final String sequenceId;
	/** */
	private Table aggregateTable;
	/** */
	private Table sequenceTable;
	/** */
	private BrowserFrame histogramChartBrowseFrame;
	/** */
	private BrowserFrame lineChartBrowseFrame;
	/** */
	private String selectedSequenceName;
	/** */
	private final SessionFactory sessionFactory;
	/** */
	private final String measureName;
	/**
	 * 
	 */
	public AggregatedSequanceTab(final String sequenceId, final String measureName, final SessionFactory sessionFactory) 
	{
		this.sequenceId = sequenceId;
		this.sessionFactory = sessionFactory;
		this.measureName = measureName;
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
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_TOTAL, Double.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_AVG, Double.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_MIN, Double.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_MAX, Double.class, null);
		aggregateTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_STD, Double.class, null);

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
		
		sequenceTable = new Table("Sequances");
		sequenceTable.setHeight(100, Unit.PERCENTAGE);
		sequenceTable.setWidth(100, Unit.PERCENTAGE);
		sequenceTable.addContainerProperty(TABLE_COLUMN_ID, Long.class, null);
		sequenceTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_NAME, String.class, null);
		sequenceTable.addContainerProperty(TABLE_COLUMN_SEQUANCE_VALUE, Double.class, null);
		
		lineChartBrowseFrame = new BrowserFrame();
		lineChartBrowseFrame.setWidth(100, Unit.PERCENTAGE);
		lineChartBrowseFrame.setHeight(100, Unit.PERCENTAGE);
		lineChartBrowseFrame.setImmediate(true);
		
		histogramChartBrowseFrame = new BrowserFrame();
		histogramChartBrowseFrame.setWidth(100, Unit.PERCENTAGE);
		histogramChartBrowseFrame.setHeight(100, Unit.PERCENTAGE);
		histogramChartBrowseFrame.setImmediate(true);

		
		final HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setHeight(100, Unit.PERCENTAGE);
		hLayout.setWidth(100, Unit.PERCENTAGE);

		final VerticalLayout vLayout = new VerticalLayout();
		vLayout.setHeight(100, Unit.PERCENTAGE);
		vLayout.setWidth(100, Unit.PERCENTAGE);
		vLayout.addComponent(histogramChartBrowseFrame);
		vLayout.addComponent(lineChartBrowseFrame);

		hLayout.addComponent(sequenceTable);
		hLayout.addComponent(vLayout);
		
		addComponent(aggregateTable);
		addComponent(hLayout);
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
					.createAlias("measures", "m", JoinType.INNER_JOIN)
					.add(Restrictions.eq("m.name", measureName))
					.setProjection(
							Projections.projectionList().
							add(Projections.groupProperty("name")).
							add(Projections.rowCount()).
							add(Projections.sum("m.value")).
							add(Projections.avg("m.value")).
							add(Projections.min("m.value")).
							add(Projections.max("m.value")).
							add(Projections.sqlProjection("STDDEV(value) as StdDev", new String[]{"StdDev"}, new Type[]{DoubleType.INSTANCE}))
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
				final Criteria c = session.createCriteria(SequenceDB.class).
						add(Restrictions.eqOrIsNull("name", selectedSequenceName)).
						createAlias("measures", "m", JoinType.INNER_JOIN).
						add(Restrictions.eq("m.name", measureName));
				final ArrayList<String> labels = new ArrayList<String>();
				final ArrayList<Long> timestamps = new ArrayList<Long>();
				final ArrayList<Double> values = new ArrayList<Double>();
				if (c != null)
				{
					for (final Iterator<SequenceDB> iterator = c.list().iterator(); iterator.hasNext();)
					{
						final SequenceDB sequenceDB = iterator.next();
						sequenceTable.addItem(getTableRow(sequenceDB), sequenceDB.getId());
						labels.add(Long.toString(sequenceDB.getId()));
						//values.add(new Double(sequenceDB.getDuration()));
						timestamps.add(new Long(sequenceDB.getEvents().get(0).getTimestamp().getTime()));
					}
				}
				trx.commit();
				final StreamResource histogramSource = new StreamResource(new HistogramChart(labels, values), selectedSequenceName + ".html");
				histogramSource.setCacheTime(0L);
				
				final StreamResource lineChartResource = new StreamResource(new LineChart(timestamps, values), selectedSequenceName + ".html");
				lineChartResource.setCacheTime(0L);

				histogramChartBrowseFrame.setSource(histogramSource);
				histogramChartBrowseFrame.markAsDirty();

				lineChartBrowseFrame.setSource(lineChartResource);
				lineChartBrowseFrame.markAsDirty();
			
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
		//row[2] = sequenceDB.getDuration();
		return row;
	}
}
