package pl.com.itsense.analysis.event.db;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import pl.com.itsense.analysis.event.BaseEventConsumer;
import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.EventProcessingListener;
import pl.com.itsense.analysis.event.ProcessingLifecycleListener;
import pl.com.itsense.analysis.event.EEngine.ProcessingLifecycle;

/**
 * 
 * @author ppretki
 * 
 */
public class DBEventConsumer extends BaseEventConsumer implements ProcessingLifecycleListener
{
    // CREATE TABLE events AS (SELECT e.timestamp, e.id, p.group1 FROM event as
    // e, event_pattern as ep, pattern as p WHERE e.eventid=ep.event_eventid AND
    // ep.patterns_patternid=p.patternid);
    /** */
    private SessionFactory sessionFactory;
    /** */
    private Session session;
    /** */
    private Transaction transaction;
    /**
     * 
     */
    @Override
    public void enter(final ProcessingLifecycle lifecycle,final EEngine engine)
    {
        switch (lifecycle)
        {
            case START:
                beginProcessing(engine);
                break;

            case FINISH:
                endProcessing(engine);
                break;
        }
    }
    
    /**
     * 
     */
    private void beginProcessing(final EEngine engine)
    {
        final Configuration cfg = configureDB();
        sessionFactory = cfg.buildSessionFactory((new ServiceRegistryBuilder()).applySettings(
            cfg.getProperties())
            .build());
        if (sessionFactory != null)
        {
            session = sessionFactory.openSession();
            if (session != null)
            {
                transaction = session.beginTransaction();
            }
        }

    }

    /**
     * 
     */
    private void endProcessing(final EEngine engine)
    {
        try
        {
            if (transaction != null)
            {
                transaction.commit();
                session.close();
            }
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
    }

    private Configuration configureDB()
    {
        final Configuration cfg = new Configuration();
        cfg.setProperty(AvailableSettings.URL, "jdbc:postgresql://localhost/adb");
        cfg.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        cfg.setProperty(AvailableSettings.DRIVER, "org.postgresql.Driver");
        cfg.setProperty(AvailableSettings.USER, "adb");
        cfg.setProperty(AvailableSettings.PASS, "adb");
        cfg.setProperty(AvailableSettings.HBM2DDL_AUTO, "create");
        cfg.addAnnotatedClass(EventDB.class);
        cfg.addAnnotatedClass(PatternDB.class);
        cfg.addAnnotatedClass(GroupDB.class);
        return cfg;
    }

    /**
     * 
     */
    @Override
    public void process(final Event event)
    {
        System.out.println("process: event = " + event);
        if (transaction != null)
        {
            final String line = event.getProperty("line");
            final EventDB eventDB = new EventDB();
            eventDB.setId(event.getId());
            eventDB.setTimestamp(new Date(event.getTimestamp()));
            eventDB.setLine(line);
            for (final String name : event.getProperties())
            {
                final int index = name.indexOf('$');
                if (index > -1)
                {
                    int groupId = -1;
                    try
                    {
                        groupId = Integer.parseInt(name.substring(index + 1));
                    }
                    catch (NumberFormatException e)
                    {
                        groupId = -1;
                        e.printStackTrace();
                    }
                    if (groupId > -1)
                    {
                        final PatternDB patternDB = new PatternDB();
                        patternDB.setPatternId(name.substring(0, index));
                        final GroupDB groupDB = new GroupDB();
                        groupDB.setGroupId(groupId);
                        groupDB.setValue(event.getProperty(name));
                        patternDB.getPatterns().add(groupDB);
                        eventDB.getPatterns().add(patternDB);
                    }
                }
            }
            try
            {
                session.save(eventDB);
                System.out.println("save = " + eventDB.getEventId());
            }
            catch (HibernateException e)
            {
                e.printStackTrace();
            }
        }
    }
}
