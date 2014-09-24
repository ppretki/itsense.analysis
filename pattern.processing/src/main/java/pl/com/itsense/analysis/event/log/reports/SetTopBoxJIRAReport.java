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

import pl.com.itsense.analysis.sequence.consumer.DescriptiveStatistics;
import pl.com.itsense.analysis.sequence.consumer.DescriptiveStatistics.Statistics;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.SequenceConsumer;

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
        PYRCU,
        TOP,
        TOP_CRITERIA
    }

    @Override
    public void create(final EventProcessingEngine engine) 
    {
        DescriptiveStatistics consumer = null;
        for (final SequenceConsumer c : engine.getSequenceConsumers())
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
        final String pyrcu = getProperty(Properties.PYRCU.name().toLowerCase());
        if ((file != null) && (consumer != null))
        {
            try
            {
                createReport(file, consumer.getStatistics(), stb + File.separatorChar, pyrcu, new DecimalFormat(formatter), Integer.parseInt(top), getComparators(topCriteria));
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
        final String pyrcu,
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

            sb.append("*Pyrcu sequence*:").append("\n");
            sb.append("{noformat}").append("\n");
            appendFromFile(pyrcu, sb);
            sb.append("{noformat}").append("\n");
        }
        
        /* SequanceId -> sequences*/
        final HashMap<String, ArrayList<DescriptiveStatistics.Statistics>> sequanceCollector = splitIntoGroups(data.values(), new GroupIdentityGenerator()
        {
            @Override
            public String getGroupId(final Statistics statistics)
            {
                return statistics.getSequanceId();
            }
        });
        
        for (final String sequanceId : sequanceCollector.keySet())
        {
            final ArrayList<DescriptiveStatistics.Statistics> sequanceStatistics = sequanceCollector.get(sequanceId);
            /* SequanceId -> sequences*/
            final HashMap<String, ArrayList<DescriptiveStatistics.Statistics>> measureCollector = splitIntoGroups(sequanceStatistics, new GroupIdentityGenerator()
            {
                @Override
                public String getGroupId(final Statistics statistics)
                {
                    return statistics.getMeasure();
                }
            });
            
            
            for (final String measure : measureCollector.keySet())
            {
                final ArrayList<DescriptiveStatistics.Statistics> list = measureCollector.get(measure);
                if ((comparators != null) && !comparators.isEmpty())
                {
                    for (final StatisticComparator comparator : comparators)
                    {
                         
                        Collections.sort(list, comparator);
                        appendStatistics(list, sequanceId, measure , comparator.values.name() , formatter, top, sb);
                    }
                }
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
    private void appendStatistics(final ArrayList<DescriptiveStatistics.Statistics> listToReport, final String sequanceId, final String measure, final String criterion, final DecimalFormat formatter, final int top, final StringBuffer sb)
    {
        int count = 0;
        sb.append("*" + sequanceId + ":" + measure + ":" + criterion + "*").append("\n");
        sb.append("||Top||ID||Count||Min||Max||Mean||Std||Skewnees||").append("\n");
        for (final DescriptiveStatistics.Statistics stats : listToReport )
        {
            sb.append("|" + count + "|");
            sb.append("|" + stats.getSequanceResolvedName() + "|");
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
        if (fileName != null)
        {
            final File file = new File(fileName);
            if (file.isFile())
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
            else
            {
                sb.append("File doesn't exist").append("\n");
            }
        }
        else
        {
            sb.append("File doesn't exist").append("\n");
        }
    }
    
    
    private HashMap<String, ArrayList<DescriptiveStatistics.Statistics>> splitIntoGroups(final Collection<DescriptiveStatistics.Statistics> statistics, final GroupIdentityGenerator identityGenerator)
    {
        final HashMap<String, ArrayList<DescriptiveStatistics.Statistics>> collector = new HashMap<String, ArrayList<DescriptiveStatistics.Statistics>>();
        for (DescriptiveStatistics.Statistics stats : statistics)
        {
            final String id = identityGenerator.getGroupId(stats);
            ArrayList<DescriptiveStatistics.Statistics> list = collector.get(id);
            if (list == null)
            {
                list = new ArrayList<DescriptiveStatistics.Statistics>();
                collector.put(id, list);
            }
            list.add(stats);
        }
        return collector;
    }
    
    private interface GroupIdentityGenerator
    {
        String getGroupId(DescriptiveStatistics.Statistics statistics);
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
            return (int)(Double.isInfinite(value) || Double.isNaN(value) ? 0 : -value);
        }
        
        
    }
    

}
