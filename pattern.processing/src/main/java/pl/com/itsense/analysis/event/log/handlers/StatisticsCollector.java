package pl.com.itsense.analysis.event.log.handlers;

import java.util.HashMap;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.EventProcessingHandler;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.log.Statistics;

/**
 * 
 * @author P.Pretki
 *
 */
public class StatisticsCollector extends PropertyHolderImpl implements EventProcessingHandler 
{
	
    /** */
    private final HashMap<String,HashMap<String,Statistics>> statistics = new HashMap<String,HashMap<String,Statistics>>();
    
    /**
     * 
     */
    public void processEvent(final Event event, final EEngine engine) 
    {
        HashMap<String,Statistics> eventRow = statistics.get(event.getId());
        if (eventRow == null)
        {
            eventRow = new HashMap<String,Statistics>();
            statistics.put(event.getId(), eventRow);
        }
        
        final Event e = engine.getEvent();
        if (e != null)
        {
        	final long t = e.getTimestamp();
            if (t > -1)
            {
            	Statistics stat = eventRow.get(e.getId());
                if (stat == null)
                {
                	stat = new Statistics();
                    eventRow.put(e.getId(), stat);
                }
                stat.add(event.getTimestamp() - t, event.getTimestamp());
            }
        }
    }
    
    /**
     * 
     * @return
     */
    public HashMap<String, HashMap<String, Statistics>> getStatistics() 
    {
        return statistics;
    }
    /**
     * 
     */
    @Override
    public String toString() 
    {
        final StringBuffer sb = new StringBuffer();
        for (final String p0 : statistics.keySet())
        {
            final HashMap<String,Statistics> stats = statistics.get(p0);
            for (final String p1 : stats.keySet())
            {
                sb.append(p0 + " -> " + p1 + ": " + stats.get(p1).toString()).append("\n");
            }
        }
        return sb.toString();
    }
    
    

    
    
    

}
