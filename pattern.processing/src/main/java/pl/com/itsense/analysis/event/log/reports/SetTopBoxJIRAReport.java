package pl.com.itsense.analysis.event.log.reports;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.ml.neuralnet.SquareNeighbourhood;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.Report;
import pl.com.itsense.analysis.event.SequenceConsumer;
import pl.com.itsense.analysis.sequence.consumer.DescriptiveStatistics;

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
        PROC
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
        if ((file != null) && (consumer != null))
        {
            try
            {
                createReport(file, consumer.getStatistics(), stb + File.separatorChar, new DecimalFormat(formatter));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } 
        }
    }

    /**
     * 
     * @param data
     */
    private void createReport(
        final String fileName, 
        final HashMap<String, DescriptiveStatistics.Statistics> data,
        final String stb,
        final DecimalFormat formatter) throws IOException 
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
        for (final String key : data.keySet()) 
        {
            final DescriptiveStatistics.Statistics stats = data.get(key);
            sb.append("*" + key + "*:").append("\n");
            sb.append("||Count||Min||Max||Mean||Std||Skewnees||").append("\n");
            sb.append("|" + (formatter == null ? stats.getCount()    : formatter.format(stats.getCount())) + "|");
            sb.append((formatter == null ? stats.getMin()      : formatter.format(stats.getMin()))   + "|");
            sb.append((formatter == null ? stats.getMax()      : formatter.format(stats.getMax()))   + "|");
            sb.append((formatter == null ? stats.getMean()     : formatter.format(stats.getMean()))  + "|");
            sb.append((formatter == null ? stats.getStd()      : formatter.format(stats.getStd()))   + "|");
            sb.append((formatter == null ? stats.getSkewness() : formatter.format(stats.getSkewness())) + "|").append("\n");
        }
        final File output = new File(fileName);
        if (output.exists()) 
        {
            Files.delete(output.toPath());
        }
        Files.write((new File(fileName)).toPath(), sb.toString().getBytes(), StandardOpenOption.CREATE);
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

}
