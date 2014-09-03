package pl.com.itsense.analysis.event.log.reports;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.ml.neuralnet.SquareNeighbourhood;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.Report;
import pl.com.itsense.analysis.event.SequenceConsumer;
import pl.com.itsense.analysis.sequence.consumer.DescriptiveStatistics;
import pl.com.itsense.analysis.sequence.consumer.DescriptiveStatistics.Statistics;

/**
 * 
 * @author ppretki
 * 
 */
public class SetTopBoxJIRAReport extends BaseReport 
{
    /** */
    private static final String UPTIME_FILE = "uptime";
    /** */
    private static final String BUILD_FILE = "build";
    
    /**
     * 
     * @author P.Pretki
     *
     */
    public enum Properties 
    {
        FILE, 
        FORMATTER, 
        PROC,
        TOP,
        TOP_CRITERIA
    }

    @Override
    public void create(final EEngine engine) 
    {
        DescriptiveStatistics consumer = null;
        for (SequenceConsumer c : engine.getSequenceConsumers())
        {
            if (c instanceof DescriptiveStatistics)
            {
                consumer = (DescriptiveStatistics)c;
                break;
            }
        }
        final String file = getProperty(Properties.FILE.name().toLowerCase());
        final String formatter = getProperty(Properties.FORMATTER.name().toLowerCase(),"#.##");
        final String stb = getProperty(Properties.PROC.name().toLowerCase());
        final String top = getProperty(Properties.TOP.name().toLowerCase(), String.valueOf(Integer.MAX_VALUE));
        final String topCriteria = getProperty(Properties.TOP_CRITERIA.name().toLowerCase());
        if ((file != null) && (consumer != null))
        {
            try
            {
                createReport(file, consumer.getStatistics(), stb + File.separatorChar, new DecimalFormat(formatter), Integer.parseInt(top), getComparators(topCriteria));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } 
        }
    }
    
    private ArrayList<StatisticComparator> getComparators(final String topCriteria)
    {
        final ArrayList<StatisticComparator> comparators = new ArrayList<StatisticComparator>();
        if ((topCriteria != null) && !topCriteria.isEmpty())
        {
            for (final String c : topCriteria.split(","))
            {
                final String crterion = c.trim();
                for (final DescriptiveStatistics.Statistics.VALUES value : DescriptiveStatistics.Statistics.VALUES.values())
                {
                    if (value.name().compareToIgnoreCase(crterion) == 0)
                    {
                        comparators.add(new StatisticComparator(value));
                        break;
                    }
                }
            }            
        }
        
        return comparators;
    }
    

    /**
     * 
     * @param data
     */
    private void createReport(
        final String fileName, 
        final HashMap<String, DescriptiveStatistics.Statistics> data,
        final String stb,
        final DecimalFormat formatter,
        final int top,
        final ArrayList<StatisticComparator> comparators) throws IOException 
    {
        final StringBuffer sb = new StringBuffer();
        if (stb != null)
        {
            sb.append("*Generation time*:").append("\n");
            sb.append("{noformat}").append("\n");
            sb.append(Calendar.getInstance().getTime().toString()).append("\n");;
            sb.append("{noformat}").append("\n");

            sb.append("*Build*:").append("\n");
            sb.append("{noformat}").append("\n");
            appendFromFile(stb + BUILD_FILE, sb);
            sb.append("{noformat}").append("\n");

            sb.append("*Uptime*:").append("\n");
            sb.append("{noformat}").append("\n");
            appendFromFile(stb + UPTIME_FILE, sb);
            sb.append("{noformat}").append("\n");
        }
        final HashMap<String,ArrayList<DescriptiveStatistics.Statistics>> collector = new HashMap<String,ArrayList<DescriptiveStatistics.Statistics>>();
        final HashMap<DescriptiveStatistics.Statistics, String> names = new HashMap<DescriptiveStatistics.Statistics, String>();
        for (final String key : data.keySet()) 
        {
           final DescriptiveStatistics.Statistics stats = data.get(key);
           names.put(stats, key);
           final String id;
           if ((comparators != null) && !comparators.isEmpty())
           {
               id = stats.getId();
           }
           else
           {
               id = key;
           }
           ArrayList<DescriptiveStatistics.Statistics> list = collector.get(id);
           if (list == null)
           {
               list = new ArrayList<DescriptiveStatistics.Statistics>();
               collector.put(id, list);
           }
           list.add(stats);
        }
        final ArrayList<String> keys = new ArrayList<String>(collector.keySet());
        Collections.sort(keys);
        for (final String key : keys) 
        {
            final ArrayList<DescriptiveStatistics.Statistics> list = collector.get(key);
            if ((comparators != null) && !comparators.isEmpty())
            {
                for (final StatisticComparator comparator : comparators)
                {
                    Collections.sort(list, comparator);
                    appendStatistics(list, key, formatter, top, names, comparator.values.name() , sb);
                }
            }
            else
            {
                appendStatistics(list, key, formatter, top, names, "" ,sb);
            }
        }
        final File output = new File(fileName);
        if (output.exists()) 
        {
            Files.delete(output.toPath());
        }
        Files.write((new File(fileName)).toPath(), sb.toString().getBytes(), StandardOpenOption.CREATE);
    }
    /** */
    private void appendStatistics(final ArrayList<DescriptiveStatistics.Statistics> listToReport, final String listName, final DecimalFormat formatter, final int top, final HashMap<DescriptiveStatistics.Statistics, String> names, final String label, final StringBuffer sb)
    {
        int count = 0;
        for (final DescriptiveStatistics.Statistics stats : listToReport )
        {
            sb.append("*" + names.get(stats) + ":" + label + "(" + count + ")*:").append("\n");
            sb.append("||Count||Min||Max||Mean||Std||Skewnees||").append("\n");
            sb.append("|" + (formatter == null ? stats.getCount()    : formatter.format(stats.getCount())) + "|");
            sb.append((formatter == null ? stats.getMin()      : formatter.format(stats.getMin()))   + "|");
            sb.append((formatter == null ? stats.getMax()      : formatter.format(stats.getMax()))   + "|");
            sb.append((formatter == null ? stats.getMean()     : formatter.format(stats.getMean()))  + "|");
            sb.append((formatter == null ? stats.getStd()      : formatter.format(stats.getStd()))   + "|");
            sb.append((formatter == null ? stats.getSkewness() : formatter.format(stats.getSkewness())) + "|").append("\n");
            if (++count >= top)
            {
                break;
            }
        }

    }
    /** */
    private void appendFromFile(final String fileName, final StringBuffer sb)
    {
        
        try
        {
            for (final String line : Files.readAllLines((new File(fileName)).toPath() , Charset.defaultCharset()))
            {
                sb.append(line).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * 
     * @author ppretki
     *
     */
    private class StatisticComparator implements Comparator<DescriptiveStatistics.Statistics>
    {
        /** */
        private final DescriptiveStatistics.Statistics.VALUES values;
        /**
         * 
         * @param values
         */
        private StatisticComparator(final DescriptiveStatistics.Statistics.VALUES values)
        {
           this.values = values;
        }
        
        @Override
        public int compare(final Statistics o1, final Statistics o2)
        {
            double value;
            switch (values)
            {
                case COUNT:
                    value = Math.signum(o1.getCount() - o2.getCount()); 
                    break;
                case MAX:
                    value = Math.signum(o1.getMax() - o2.getMax()); 
                    break;
                case MIN:
                    value = Math.signum(o1.getMin() - o2.getMin()); 
                    break;
                case MEAN:
                    value = Math.signum(o1.getMean() - o2.getMean()); 
                    break;
                case STD:
                    value = Math.signum(o1.getStd() - o2.getStd()); 
                    break;
                case SKEWNESS:
                    value = Math.signum(o1.getSkewness() - o2.getSkewness()); 
                    break;
                 default:
                     value = 0.0;
                     break;

            }
            return (int)(Double.isFinite(value) ? -value : 0);
        }
        
        
    }
    

}
