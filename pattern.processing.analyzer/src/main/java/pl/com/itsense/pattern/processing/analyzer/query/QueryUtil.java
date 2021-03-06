package pl.com.itsense.pattern.processing.analyzer.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import pl.com.itsense.eventprocessing.db.SequenceDB;

/**
 * 
 * @author ppretki
 *
 */
public class QueryUtil 
{
	/**
	 * 
	 * @param sessionFactory
	 * @return
	 */
	public static String[] getSequenceIds(final SessionFactory sessionFactory)
	{
		final ArrayList<String> result = new ArrayList<String>();
		Session session = null;
		Transaction trx = null;
		try
		{
			session = sessionFactory.openSession();
			trx = session.beginTransaction();
			List list = session.createCriteria(SequenceDB.class).setProjection(Projections.groupProperty("sequenceId")).list();
			if ((list != null) && !list.isEmpty())	
			{
				for (Object obj : list)
				{
					result.add(obj.toString());
				}
			}
			 
		}
		catch(HibernateException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{

			
			if (trx != null)
			{
				try
				{
					trx.commit();
				}
				catch(HibernateException e){}
			}

			if (session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException e){}
			}
		}
		return result.toArray(new String[0]);
	}
	
	
	/**
	 * 
	 * @param sessionFactory
	 * @return
	 */
	public static String[] getMeasureNames(final SessionFactory sessionFactory, final String sequenceId)
	{
		final ArrayList<String> result = new ArrayList<String>();
		Session session = null;
		Transaction trx = null;
		try
		{
			session = sessionFactory.openSession();
			trx = session.beginTransaction();
			List list = session.createCriteria(SequenceDB.class).add(Restrictions.eq("sequenceId", sequenceId)).createAlias("measures", "m", JoinType.INNER_JOIN).setProjection(Projections.groupProperty("m.name")).list();
			if ((list != null) && !list.isEmpty())	
			{
				for (Object obj : list)
				{
					System.out.println("getMeasureNames: " + obj);
					result.add(obj.toString());
				}
			}
			 
		}
		catch(HibernateException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{

			
			if (trx != null)
			{
				try
				{
					trx.commit();
				}
				catch(HibernateException e){}
			}

			if (session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException e){}
			}
		}
		if (result.size() > 1) Collections.sort(result);
		return result.toArray(new String[0]);
	}

}
