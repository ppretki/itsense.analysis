package pl.com.itsense.analysis.sequence.consumer;

import java.util.LinkedList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import pl.com.itsense.analysis.event.BaseSequanceConsumer;
import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Sequence;
import pl.com.itsense.analysis.event.db.EventDB;
import pl.com.itsense.analysis.event.db.GroupDB;
import pl.com.itsense.analysis.event.db.PatternDB;
import pl.com.itsense.analysis.event.db.SequenceDB;
/**
 * 
 * @author ppretki
 *
 */
public class DBRecorder extends BaseSequanceConsumer
{
    private static final String HIBERNATE_PROPERTY_PREFIX = "hibernate"; 
    /** */
    private static int BATCH_SIZE = 1000;
    /** */
    private LinkedList<SequenceDB> cache = new LinkedList<SequenceDB>();
    /** */
    private SessionFactory sessionFactory;
    /** */
    private Session session;

    /** */
    private Configuration configureDB()
    {
        final Configuration cfg = new Configuration();
        for (final String property : getProperties())
        {
            if (property.startsWith(HIBERNATE_PROPERTY_PREFIX))
            {
                cfg.setProperty(property, getProperty(property));
            }
        }
        cfg.addAnnotatedClass(SequenceDB.class);
        cfg.addAnnotatedClass(EventDB.class);
        cfg.addAnnotatedClass(PatternDB.class);
        cfg.addAnnotatedClass(GroupDB.class);
        return cfg;
    }

    @Override
    public void consume(final Sequence sequence)
    {
        if ((session != null) && (sequence != null))
        {
            cache.add(new SequenceDB(sequence));
            if (cache.size() == BATCH_SIZE)
            {
                pruneCache();
            }
        }

    }

    
    @Override
    protected void processingStart(EEngine engine)
    {
        final Configuration cfg = configureDB();
        sessionFactory = cfg.buildSessionFactory((new ServiceRegistryBuilder()).applySettings(cfg.getProperties()).build());
        if (sessionFactory != null)
        {
            session = sessionFactory.openSession();
        }
    }
    
    @Override
    protected void processingFinish(EEngine engine)
    {
        try
        {
            if (session != null)
            {
                pruneCache();
                session.close();
            }
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }

    }
    
    /**
     * 
     */
    private void pruneCache()
    {
        final long start = System.currentTimeMillis();
        Transaction trx = null;
        try
        {
            trx = session.beginTransaction();
            if (trx != null)
            {
                for (final SequenceDB sequence : cache)
                {
                    session.save(sequence);
                }
                session.flush();
                session.clear();
                trx.commit();
            }
            System.out.println("save of " + cache.size() + " sequences took " + (System.currentTimeMillis() - start));
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
        finally
        {
            cache.clear();
        }
        
    }

}
