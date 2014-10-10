package pl.com.itsense.eventprocessing.consumer;

import java.util.LinkedList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import pl.com.itsense.eventprocessing.api.Event;
import pl.com.itsense.eventprocessing.api.EventConsumer;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.EventProcessingListener;
l.com.itsense.eventprocessing.api.EventProcessingEngine;

/**
 * 
 * @author ppretki
 *
 */
public class SQLConsumer implements EventConsumer, EventProcessingListener
{
    private static final String HIBERNATE_PROPERTY_PREFIX = "hibernate";
    /** */
    private static int BATCH_SIZE = 1000;
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
        return cfg;
    }


    @Override
    protected void processingStart(final EventProcessingEngine engine)
    {
        final Configuration cfg = configureDB();
        sessionFactory = cfg.buildSessionFactory((new ServiceRegistryBuilder()).applySettings(cfg.getProperties())
            .build());
        if (sessionFactory != null)
        {
            session = sessionFactory.openSession();
        }
    }

    @Override
    protected void processingFinish(final EventProcessingEngine engine)
    {
        try
        {
            if (session != null)
            {
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
     * @return
     */
    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }


    @Override
    public void process(Event event)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void beginProcessing(EventProcessingEngine engine)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void endProcessing(EventProcessingEngine engine)
    {
        // TODO Auto-generated method stub
        
    }

}
