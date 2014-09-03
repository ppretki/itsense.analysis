package pl.com.itsense.analysis.event.log.reports;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.HashMap;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.Report;
import pl.com.itsense.analysis.sequence.consumer.DescriptiveStatistics;

/**
 * 
 * @author ppretki
 * 
 */
public class HTMLReport extends BaseReport 
{
    /**
     * 
     * @author P.Pretki
     *
     */
    public enum Properties 
    {
        FILE, FORMATTER
    }

    @Override
    public void create(final EEngine engine) 
    {
    }

    /**
     * 
     * @param data
     */
    public void createReport(final String fileName, final HashMap<String, DescriptiveStatistics.Statistics> data, final DecimalFormat formatter) 
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

        for (final String id : data.keySet()) 
        {
            final DescriptiveStatistics.Statistics stats = data.get(id);
            sb.append("<tr>").append("\n");
            sb.append("<td><b>" + id + "</b></td>\n");
            sb.append("<td> " + stats.getCount() + " </td>\n");
            sb.append("<td> " + (formatter == null ? stats.getCount() : formatter.format(stats.getMin())) + " </td>\n");
            sb.append("<td> " + (formatter == null ? stats.getMax() : formatter.format(stats.getMax())) + " </td>\n");
            sb.append("<td> " + (formatter == null ? stats.getMean() : formatter.format(stats.getMean())) + " </td>\n");
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
