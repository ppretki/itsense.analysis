package pl.com.itsense.analysis.event.log.handler.action;

import java.util.HashMap;

import pl.com.itsense.analysis.event.Action;
import pl.com.itsense.analysis.event.ActionProcessingHandler;
import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.log.Statistics;

/**
 * 
 * @author P.Pretki
 *
 */
public class StatisticsCollector extends PropertyHolderImpl implements ActionProcessingHandler 
{
	
    /** */
    private final HashMap<String,Statistics> statistics = new HashMap<String,Statistics>();
    
    /**
     * 
     */
    public void processAction(final Action action, final EEngine engine) 
    {
    	final String id = action.getProperty("id") != null ? action.getProperty("id") : action.getId();
        Statistics actionStatistics = statistics.get(id);
        if (actionStatistics == null)
        {
        	actionStatistics = new Statistics();
            statistics.put(id, actionStatistics);
        }
        actionStatistics.add(action.getEvent(Action.Status.CLOSE).getTimestamp() - action.getEvent(Action.Status.OPEN).getTimestamp());
    }
    
    /**
     * 
     * @return
     */
    public HashMap<String, Statistics> getStatistics() 
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
            final Statistics stats = statistics.get(p0);
            sb.append("action: " + p0 +", statistics: \n"  + stats.toString()).append("\n");
        }
        return sb.toString();
    }
    
    

    
    
    

}
