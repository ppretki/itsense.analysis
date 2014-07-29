package pl.com.itsense.analysis.event.analytics;


import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;

import pl.com.itsense.analysis.event.BaseSequanceConsumer;
import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Sequence;
import pl.com.itsense.analysis.event.EEngine.ProcessingLifecycle;

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
        System.out.println("Stat: " + sequence);
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
    public void enter(final ProcessingLifecycle lifecycle,final EEngine engine)
    {
        switch (lifecycle)
        {
            case FINISH:
                endProcessing(engine);
                break;
            default:
        }
    }
    
    /**
     * 
     */
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
        /** */
        private Mean mean = new Mean();
        /** */
        private StandardDeviation std = new StandardDeviation();
        /** */
        private Min min = new Min();
        /** */
        private Max max = new Max();
        /** */
        private Skewness skewness = new Skewness();

        /**
         * 
         * @param value
         */
        private void add(final double value)
        {
            mean.increment(value);
            std.increment(value);
            min.increment(value);
            max.increment(value);
            skewness.increment(value);
            count++;
        }
        
        @Override
        public String toString()
        {
            final StringBuffer sb = new StringBuffer();
            sb.append("count = " + count).append("\n");
            sb.append("mean = " + mean.getResult()).append("\n");
            sb.append("std = " + std.getResult()).append("\n");
            sb.append("min = " + min.getResult()).append("\n");
            sb.append("max = " + max.getResult()).append("\n");
            sb.append("skewness = " + skewness.getResult()).append("\n");

            return sb.toString();
        }
        /**
         * 
         * @return
         */
        public int getCount()
        {
            return count;
        }
        
        /**
         * 
         * @return
         */
        public double getMean()
        {
            return mean.getResult();
        }
        /**
         * 
         * @return
         */
        public double getStd()
        {
            return std.getResult();
        }
        /**
         * 
         * @return
         */
        public double getSkewness()
        {
            return skewness.getResult();
        }
        /**
         * 
         * @return
         */
        public double getMax()
        {
            return max.getResult();
        }
        /**
         * 
         * @return
         */
        public double getMin()
        {
            return min.getResult();
        }

    }
}
