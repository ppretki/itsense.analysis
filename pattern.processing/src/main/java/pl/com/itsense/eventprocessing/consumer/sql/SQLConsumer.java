package pl.com.itsense.eventprocessing.consumer.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import pl.com.itsense.eventprocessing.api.Event;
import pl.com.itsense.eventprocessing.api.EventConsumer;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.EventProvider;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycle;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycleListener;
import pl.com.itsense.eventprocessing.provider.rexpression.RExpression;
import pl.com.itsense.eventprocessing.provider.rexpression.RExpressionEvent;
import pl.com.itsense.eventprocessing.provider.rexpression.RExpressionProvider;

/**
 * 
 * @author ppretki
 *
 */
public class SQLConsumer implements EventConsumer, ProcessingLifecycleListener
{
    private HashMap<RExpression, SQLTable> tables = new HashMap<RExpression ,SQLTable>(); 
    /** */
    private String url;
    /** */
    private String driver;
    /** */
    private String user;
    /** */
    private String password;
    /** */
    private Connection connection;
    

    @Override
    public void process(final Event event)
    {
        if (event instanceof RExpressionEvent)
        {
            final RExpressionEvent e = (RExpressionEvent) event;
            final SQLTable table = tables.get(e.getRExpression());
            if (table != null)
            {
                final SQLTuple tuple = table.insert(e);
                for (final SQLTable t : tables.values())
                {
                    t.setCrossReferences(tuple);
                }
            }
        }
    }


    private void beginProcessing(final EventProcessingEngine engine)
    {
        try
        {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            for (final EventProvider provider : engine.getProviders())
            {
                if (provider instanceof RExpressionProvider)
                {
                    for (final RExpression expression : ((RExpressionProvider)provider).getRExpressions())
                    {
                        tables.put(expression, new SQLTable(expression, connection));
                    }
                }
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        for (final SQLTable table : tables.values())
        {
            table.init(tables.values());
        }
    }


    private void endProcessing(final EventProcessingEngine engine)
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    /**
     * 
     * @param driver
     */
    public void setDriver(final String driver)
    {
        this.driver = driver;
    }
    /**
     * 
     * @param password
     */
    public void setPassword(final String password)
    {
        this.password = password;
    }
    /**
     * 
     * @param user
     */
    public void setUser(final String user)
    {
        this.user = user;
    }

    /**
     * 
     * @param url
     */
    public void setUrl(final String url)
    {
        this.url = url;
    }

    @Override
    public void enter(final ProcessingLifecycle lifecycle, final EventProcessingEngine engine)
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
}
