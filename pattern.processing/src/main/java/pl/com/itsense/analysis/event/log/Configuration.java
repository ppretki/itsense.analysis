package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;
/**
 * 
 * @author ppretki
 *
 */
public class Configuration
{
    /** */
    private final ArrayList<EventConf> events = new ArrayList<EventConf>();
    /** */
    private final ArrayList<FileConf> files = new ArrayList<FileConf>();
    /** */
    private final ArrayList<EventConsumerConf> eventConsumers = new ArrayList<EventConsumerConf>();

    /**
     * 
     * @param xmlConfigFile
     */
    public static final Configuration parse(final File file)
    {
        Configuration configuration = null;

        final Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("config",               "pl.com.itsense.analysis.event.log.Configuration");
        digester.addObjectCreate("config/event",         "pl.com.itsense.analysis.event.log.EventConf");
        digester.addObjectCreate("config/event/pattern", "pl.com.itsense.analysis.event.log.PatternConf");
        digester.addObjectCreate("config/file",          "pl.com.itsense.analysis.event.log.FileConf");
        digester.addObjectCreate("config/consumer",      "pl.com.itsense.analysis.event.log.EventConsumerConf");

        digester.addSetNext("config/event",              "add", "pl.com.itsense.analysis.event.log.EventConf");
        digester.addSetNext("config/event/pattern",      "add", "pl.com.itsense.analysis.event.log.PatternConf");
        digester.addSetNext("config/file",               "add", "pl.com.itsense.analysis.event.log.FileConf");
        digester.addSetNext("config/consumer",           "add", "pl.com.itsense.analysis.event.log.EventConsumerConf");
        digester.addSetNext("config/consumer/property",  "add", "pl.com.itsense.analysis.event.log.PropertyConf");

        digester.addSetProperties("config");
        digester.addSetProperties("config/event");
        digester.addSetProperties("config/file");
        digester.addSetProperties("config/consumer");
        digester.addSetProperties("config/event/pattern");

        try
        {
            configuration = digester.parse(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        return configuration;
    }

    /**
     * 
     * @return
     */
    public ArrayList<EventConf> getEvents()
    {
        return events;
    }

    /**
     * 
     */
    public void add(final EventConf event)
    {
        if (!events.contains(event) && (event != null))
        {
            events.add(event);
        }
    }

    /**
     * 
     * @return
     */
    public ArrayList<FileConf> getFiles()
    {
        return files;
    }

    /**
     * 
     * @param file
     */
    public void add(final FileConf file)
    {
        if (!files.contains(file) && (file != null))
        {
            files.add(file);
        }
    }

    public ArrayList<EventConsumerConf> getEventConsumers()
    {
        return eventConsumers;
    }

    /**
     *
     */
    public void add(final EventConsumerConf consumer)
    {
        if (!eventConsumers.contains(consumer) && (consumer != null))
        {
            eventConsumers.add(consumer);
        }
    }

    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("-------- Configuration: Events --------").append("\n");
        for (final EventConf event : events)
        {
            sb.append(event).append("\n");
        }
        sb.append("-------- Configuration: Files --------").append("\n");
        for (final FileConf file : files)
        {
            sb.append(file).append("\n");
        }
        sb.append("-------- Configuration: EventConsumers --------").append("\n");
        for (final EventConsumerConf consumer : eventConsumers)
        {
            sb.append(consumer).append("\n");
        }
        return sb.toString();
    }

}
