package pl.com.itsense.analysis.event.log.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import pl.com.itsense.analysis.sequence.consumer.DescriptiveStatistics;
import pl.com.itsense.analysis.sequence.consumer.DescriptiveStatistics.Statistics;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.SequenceConsumer;

/**
 * 
 * @author ppretki
 * 
 */
public class PlainTextReport extends BaseReport 
{
    /**
     * 
     * @author P.Pretki
     *
     */
    public enum Properties 
    {
        FORMATTER
    }

    @Override
    public void create(final EventProcessingEngine engine) 
    {
        DescriptiveStatistics statistics = null;
        for (final SequenceConsumer consumer : engine.getSequenceConsumers())
        {
            if (consumer instanceof DescriptiveStatistics)
            {
                statistics = (DescriptiveStatistics) consumer;
            }
        }
        if (statistics != null)
        {
            final HashMap<String, Statistics> stats = statistics.getStatistics();
            final HashMap<Statistics,String> inverseIndex = new HashMap<Statistics,String>();
            for (final String statId  : stats.keySet())
            {
                inverseIndex.put(stats.get(statId), statId);
            }
            final ArrayList<Statistics> list = new ArrayList<Statistics>(stats.values());
            if (list.size() > 1)
            {
                Collections.sort(list, DescriptiveStatistics.COMPARATOR_COUNT_ASC);
            }
            final int TOP = 10;
            for (int i = 0 ; i < Math.min(list.size(),TOP); i++)
            {
                System.out.println("Statistics: " + inverseIndex.get(list.get(i)) + "\n");
                System.out.println(list.get(i));
            }
        }
    }

    /**
     * 
     * @param data
     */
    public void createReport(final HashMap<String, DescriptiveStatistics.Statistics> data, final DecimalFormat formatter) 
    {
        final StringBuffer sb = new StringBuffer();
        for (final String id : data.keySet()) 
        {
            final DescriptiveStatistics.Statistics stats = data.get(id);
            sb.append(stats.getCount());
            sb.append(formatter == null ? stats.getCount() : formatter.format(stats.getMin()));
            sb.append(formatter == null ? stats.getMax() : formatter.format(stats.getMax()));
            sb.append(formatter == null ? stats.getMean() : formatter.format(stats.getMean()));
            sb.append(formatter == null ? stats.getStd() : formatter.format(stats.getStd()));
        }
        System.out.println(sb.toString());
    }

}
