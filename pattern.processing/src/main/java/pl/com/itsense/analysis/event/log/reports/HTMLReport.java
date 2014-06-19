package pl.com.itsense.analysis.event.log.reports;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.HashMap;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.EventProcessingHandler;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.Report;
import pl.com.itsense.analysis.event.log.Statistics;
import pl.com.itsense.analysis.event.log.handlers.StatisticsCollector;

/**
 * 
 * @author ppretki
 *
 */
public class HTMLReport extends PropertyHolderImpl implements Report 
{
	/**
	 * 
	 */
	public enum Properties 
	{
		FILE,
		FORMATTER
	}
	
	@Override
	public void create(final EEngine engine) 
	{
		for (final EventProcessingHandler handler : engine.getHandlers())
		{
			if (handler instanceof StatisticsCollector)
			{
				final String fileName = getProperty(Properties.FILE.name().toLowerCase());
				final String forrmatter = getProperty(Properties.FORMATTER.name().toLowerCase());
				final HashMap<String, HashMap<String, Statistics>> statistics = ((StatisticsCollector)handler).getStatistics();
				createReport(fileName, statistics, forrmatter != null ? new DecimalFormat(forrmatter) : null);
			}
		}
	}
	
	
    /**
     * 
     * @param data
     */
    public void createReport(final String fileName, final HashMap<String,HashMap<String,Statistics>> data, final DecimalFormat formatter)
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
                                        sb.append("<span> std = " + statistics.getStd() + "</span><br>\n");
                                    }
                                    else
                                    {
                                        sb.append("<span> avg = " + formatter.format(statistics.getAvg()) + "</span><br>\n");
                                        sb.append("<span> avg = " + formatter.format(statistics.getStd()) + "</span><br>\n");
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
    
            
            sb.append("<table>\n");
            for (final String pattern0 : patterns)
            {
                final HashMap<String,Statistics> stats = data.get(pattern0);
                for (final String pattern1 : patterns)
                {
                    final Statistics statistics = stats.get(pattern1);
                    if (statistics != null)
                    {
                        sb.append("<tr>\n");
                        sb.append("<td><b>" + pattern0 + "</b> -> <b>" + pattern1 + "</b> </td>\n");
                        sb.append("<td>" + statistics.getMinLine() + " </td>\n");
                        sb.append("<td>" + statistics.getMaxLine() + " </td>\n");
                        sb.append("</tr>\n");
                    }
                }
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
