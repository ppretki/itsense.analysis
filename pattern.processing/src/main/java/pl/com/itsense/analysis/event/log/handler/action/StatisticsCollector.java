package pl.com.itsense.analysis.event.log.handler.action;

import java.util.HashMap;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.Transition;
import pl.com.itsense.analysis.event.TransitionAnalyzer;
import pl.com.itsense.analysis.event.log.Statistics;

/**
 * 
 * @author P.Pretki
 *
 */
public class StatisticsCollector extends PropertyHolderImpl implements TransitionAnalyzer 
{
	
    /** */
    private final HashMap<String,HashMap<String,Statistics>> statistics = new HashMap<String,HashMap<String,Statistics>>();
    
    /**
     * 
     */
    public void process(final Transition transition, final EEngine engine) 
    {
        final String rowId = transition.from().getId();
        final String colId = transition.to().getId();
        HashMap<String, Statistics> row = statistics.get(rowId);
        if (row == null)
        {
            row = new HashMap<String,Statistics>();
            statistics.put(rowId, row);
        }
        Statistics stats = row.get(colId);
        if (stats == null)
        {
            stats = new Statistics();
            row.put(colId, stats);
        }
        stats.add(transition.to().activationTimestamp() - transition.from().activationTimestamp());
    }
    
    /**
     * 
     */
    @Override
    public String toString() 
    {
        final StringBuffer sb = new StringBuffer();
        for (final String rowId : statistics.keySet())
        {
            final HashMap<String,Statistics> row = statistics.get(rowId);
            for (final String colId : row.keySet())
            {
                final Statistics stats = row.get(colId);
                sb.append(rowId + " ->" + colId +": \n"  + stats.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
