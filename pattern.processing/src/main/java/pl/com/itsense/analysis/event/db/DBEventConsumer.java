package pl.com.itsense.analysis.event.db;

import java.util.Date;
import java.util.LinkedList;

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
    /*
SELECT 
  eventdb.eventid, 
  eventdb."timestamp", 
  patterndb.patternid, 
  groupdb.value,
  groupdb.groupid
FROM 
  public.eventdb, 
  public.eventdb_patterndb, 
  public.patterndb, 
  public.patterndb_groupdb, 
  public.groupdb
WHERE 
  eventdb.id = eventdb_patterndb.eventdb_id AND
  eventdb_patterndb.patterns_id = patterndb.id AND
  patterndb.id = patterndb_groupdb.patterndb_id AND
  patterndb_groupdb.patterns_id = groupdb.id AND
  (patterndb.patternid = 'activate' OR (patterndb.patternid = 'pathChange' AND groupid=2))
  ORDER by eventdb."timestamp"
  LIMIT 1000;
    */
    
    /** */
    private static int BATCH_SIZE = 1000;
    /** */
    private LinkedList<EventDB> cache = new LinkedList<EventDB>();
    /** */
    private SessionFactory sessionFactory;
    /** */
    private Session session;
    
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
        }

    }

    /**
     * 
     */
    private void endProcessing(final EEngine engine)
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
        //System.out.println("process: event = " + event);
        if (session != null)
        {
            final String line = event.getProperty("line");
            final EventDB eventDB = new EventDB();
            eventDB.setEventId(event.getId());
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
            
            cache.add(eventDB);
            if (cache.size() == BATCH_SIZE)
            {
                pruneCache();
            }
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
                for (final EventDB e : cache)
                {
                    session.save(e);
                }
                session.flush();
                session.clear();
                trx.commit();
            }
            System.out.println("save of " + cache.size() + " events took " + (System.currentTimeMillis() - start));
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
