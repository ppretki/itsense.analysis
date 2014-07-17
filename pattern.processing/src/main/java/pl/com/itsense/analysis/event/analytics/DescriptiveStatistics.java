package pl.com.itsense.analysis.event.analytics;


import java.util.HashMap;

import pl.com.itsense.analysis.event.BaseSequanceConsumer;
import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Sequence;

/**
 * 
 * @author ppretki
 *
 */
public class DescriptiveStatistics extends BaseSequanceConsumer
{
    /** */
    private HashMap<String,Statistics> statistics = new HashMap<String,Statistics>();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void consume(final Sequence sequence)
    {
        final String id = sequence != null ? sequence.getResolvedName() : null;
        if (id != null)
        {
            Statistics stats = statistics.get(id);
            if (stats == null)
            {
                stats = new Statistics();
                statistics.put(id, stats);
            }
            stats.add(sequence.getDuration());
        }
        //System.out.println("Stat: " + sequence);
    }
    /**
     * 
     */
    @Override
    public String toString()
    {
        return "DescriptiveStatistics: " + super.toString();
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
    public void endProcessing(final EEngine engine)
    {
        for (final String seqName : statistics.keySet())
        {
            System.out.println(seqName + " : " + statistics.get(seqName));
        }
    }
    /**
     * 
     * @author ppretki
     *
     */
    public static class Statistics
    {
        /** */
        private int count;
        /**
         * 
         * @param value
         */
        private void add(final double value)
        {
            count++;
        }
        
        @Override
        public String toString()
        {
            final StringBuffer sb = new StringBuffer();
            sb.append("count = " + count).append("\n");
            return sb.toString();
        }
    }
}
