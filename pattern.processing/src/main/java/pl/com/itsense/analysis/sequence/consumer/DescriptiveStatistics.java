package pl.com.itsense.analysis.sequence.consumer;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import pl.com.itsense.analysis.event.log.reports.PlainTextReport;

/**
 * 
 * @author ppretki
 *
 */
public class DescriptiveStatistics extends BaseSequanceConsumer
{
    
    public static Comparator<Statistics> COMPARATOR_COUNT_ASC = new Comparator<DescriptiveStatistics.Statistics>()
    {
        @Override
        public int compare(final Statistics s1, final Statistics s2)
        {
            return s2.getCount() - s1.getCount();
        }
    }; 
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
            final String[] measureNames  = sequence.getMeasureNames();
            if ((measureNames != null) && (measureNames.length > 0))
            {
                for (final String measure : measureNames)
                {
                    final String st = id + ":" + measure;
                    final Double value = sequence.getMeasure(measure);
                    if (value != null)
                    {
                        Statistics stats = statistics.get(st);
                        if (stats == null)
                        {
                            stats = new Statistics(sequence.getId(), sequence.getName(), sequence.getResolvedName(), measure);
                            statistics.put(st, stats);
                        }
                        stats.add(sequence.getMeasure(measure));
                    }
                }
            }
        }
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
        new PlainTextReport().create(engine);
    }
    /**
     * 
     * @author ppretki
     *
     */
    public static class Statistics
    {
        public enum VALUES
        {
            COUNT,
            MEAN,
            STD,
            MAX,
            MIN,
            SKEWNESS
        }
        /** */
        private final String sequanceId;
        /** */
        private final String sequanceName;
        /** */
        private final String sequanceResolvedName;
        /** */
        private final String measure;
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

        
        public Statistics(final String sequanceId, final String sequanceName, final String sequanceResolvedName, final String measure)
        {
            this.sequanceId = sequanceId;
            this.sequanceName = sequanceName;
            this.sequanceResolvedName  = sequanceResolvedName;
            this.measure = measure;
        }
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
            sb.append("count = " + getCount()).append("\n");
            sb.append("mean = " + getMean()).append("\n");
            sb.append("std = " + getStd()).append("\n");
            sb.append("min = " + getMin()).append("\n");
            sb.append("max = " + getMax()).append("\n");
            sb.append("skewness = " + getSkewness()).append("\n");

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
            return getDoubleValue(mean.getResult());
        }
        /**
         * 
         * @return
         */
        public double getStd()
        {
            return getDoubleValue(std.getResult());
        }
        /**
         * 
         * @return
         */
        public double getSkewness()
        {
            return getDoubleValue(skewness.getResult());
        }
        /**
         * 
         * @return
         */
        public double getMax()
        {
            return getDoubleValue(max.getResult());
        }
        /**
         * 
         * @return
         */
        public double getMin()
        {
            return getDoubleValue(min.getResult());
        }
        /**
         * 
         * @return
         */
        private double getDoubleValue(final double value)
        {
            return Double.isInfinite(value) || Double.isNaN(value) ? 0.0 : value;
        }
        /**
         * 
         * @return
         */
        public String getSequanceId()
        {
            return sequanceId;
        }
        /**
         * 
         * @return
         */
        public String getSequanceName()
        {
            return sequanceName;
        }
        /**
         * 
         * @return
         */
        public String getSequanceResolvedName()
        {
            return sequanceResolvedName;
        }
        /**
         * 
         * @return
         */
        public String getMeasure()
        {
            return measure;
        }
    }
}
