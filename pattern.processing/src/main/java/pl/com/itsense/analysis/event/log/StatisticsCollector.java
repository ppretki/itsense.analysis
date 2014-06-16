package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.HashMap;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.EventProcessingHandler;

/**
 * 
 * @author P.Pretki
 *
 */
public class StatisticsCollector implements EventProcessingHandler 
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
        for (final String id : engine.getEventIds())
        {
            final Event e = engine.getEvent(id);
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
                    stat.add(event.getTimestamp() - t, event.getTimestamp() , null);
                }
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
    
    
    /**
     * 
     * @param data
     */
    public static void createReport(final String fileName, final HashMap<String,HashMap<String,Statistics>> data, final DecimalFormat formatter)
    {
            final StringBuffer sb = new StringBuffer();
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<style type=\"text/css\">\n");
            sb.append("      \n");
            sb.append("</style>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<table>\n");
            sb.append("<tr>\n");
            sb.append("<td></td>\n");
            final String[] patterns = data.keySet().toArray(new String[0]);
            for (final String pattern : patterns)
            {
                    sb.append("<td><b>"+ pattern +"</b></td>\n");
            }
            sb.append("</tr>\n");
            for (final String pattern0 : patterns)
            {
                    sb.append("<tr>").append("\n");
                    sb.append("<td><b>" + pattern0 + "</b></td>\n");
                    final HashMap<String,Statistics> stats = data.get(pattern0);
                    for (final String pattern1 : patterns)
                    {
                            final Statistics statistics = stats.get(pattern1);
                            if (statistics != null)
                            {
                                    sb.append("<td>\n");
                                    sb.append("<span> count = " + statistics.getCount() + "</span><br>\n");
                                    if (formatter == null)
                                    {
                                        sb.append("<span> avg = " + statistics.getAvg() + "</span><br>\n");    
                                    }
                                    else
                                    {
                                        sb.append("<span> avg = " + formatter.format(statistics.getAvg()) + "</span><br>\n");
                                    }
                                    
                                    sb.append("<span> max = " + statistics.getMax() + "</span><br>\n");
                                    sb.append("<span> min = " + statistics.getMin() + "</span>\n");
                                    sb.append("</td>\n");
                            }
                            else
                            {
                                    sb.append("<td> --- </td>\n");
                            }
                    }
                    sb.append("</tr>\n");
            }
            sb.append("</table>\n");
            sb.append("</body>\n");
            sb.append("</html>\n");
            try 
            {
                final File output = new File(fileName);
                if (output.exists())
                {
                        Files.delete(output.toPath());
                }
                Files.write((new File(fileName)).toPath(), sb.toString().getBytes(), StandardOpenOption.CREATE);
            } 
            catch (IOException e) 
            {
                    e.printStackTrace();
            }
    }

}
