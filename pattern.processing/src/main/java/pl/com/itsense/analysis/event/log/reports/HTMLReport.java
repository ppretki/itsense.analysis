package pl.com.itsense.analysis.event.log.reports;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.HashMap;

import pl.com.itsense.analysis.event.ActionProcessingHandler;
import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.Report;
import pl.com.itsense.analysis.event.log.Statistics;
import pl.com.itsense.analysis.event.log.handler.action.StatisticsCollector;

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
		for (final ActionProcessingHandler handler : engine.getActionHandlers())
		{
			if (handler instanceof StatisticsCollector)
			{
				final String fileName = getProperty(Properties.FILE.name().toLowerCase());
				final String forrmatter = getProperty(Properties.FORMATTER.name().toLowerCase());
				final HashMap<String, Statistics> statistics = ((StatisticsCollector)handler).getStatistics();
				createReport(fileName, statistics, forrmatter != null ? new DecimalFormat(forrmatter) : null);
			}
		}
	}
	
	
    /**
     * 
     * @param data
     */
    public void createReport(final String fileName, final HashMap<String,Statistics> data, final DecimalFormat formatter)
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
            sb.append("<th> Action Id </th>\n");
            sb.append("<th> Count </th>\n");
            sb.append("<th> MIN </th>\n");
            sb.append("<th> MAX </th>\n");
            sb.append("<th> AVG </th>\n");
            sb.append("<th> STD </th>\n");
            sb.append("</tr>\n");
            
            for (final String actionId : data.keySet())
            {
                final Statistics stats = data.get(actionId);
            	sb.append("<tr>").append("\n");
            	sb.append("<td><b>" + actionId + "</b></td>\n");
                sb.append("<td> " + stats.getCount() + " </td>\n");
                sb.append("<td> " + (formatter == null ? stats.getMin() : formatter.format(stats.getMin())) + " </td>\n");
                sb.append("<td> " + (formatter == null ? stats.getMax() : formatter.format(stats.getMax())) + " </td>\n");
                sb.append("<td> " + (formatter == null ? stats.getAvg() : formatter.format(stats.getAvg())) + " </td>\n");
                sb.append("<td> " + (formatter == null ? stats.getStd() : formatter.format(stats.getStd())) + " </td>\n");
            	sb.append("</tr>").append("\n");
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
