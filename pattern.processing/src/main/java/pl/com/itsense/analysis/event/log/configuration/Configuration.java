package pl.com.itsense.analysis.event.log.configuration;

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
    /** */
    private final ArrayList<SequenceConf> sequences = new ArrayList<SequenceConf>();
    /** */
    private final ArrayList<SequenceConsumerConf> sequenceConsumers = new ArrayList<SequenceConsumerConf>();
    /**
     * 
     * @param xmlConfigFile
     */
    public static final Configuration parse(final File file)
    {
        Configuration configuration = null;

        final Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("config",                          "pl.com.itsense.analysis.event.log.configuration.Configuration");
        digester.addObjectCreate("config/event",                    "pl.com.itsense.analysis.event.log.configuration.EventConf");
        digester.addObjectCreate("config/event/pattern",            "pl.com.itsense.analysis.event.log.configuration.PatternConf");
        digester.addObjectCreate("config/file",                     "pl.com.itsense.analysis.event.log.configuration.FileConf");
        digester.addObjectCreate("config/consumer",                 "pl.com.itsense.analysis.event.log.configuration.EventConsumerConf");
        digester.addObjectCreate("config/consumer/property",        "pl.com.itsense.analysis.event.log.configuration.PropertyConf");
        digester.addObjectCreate("config/sequence",                 "pl.com.itsense.analysis.event.log.configuration.SequenceConf");
        digester.addObjectCreate("config/sequence/term",            "pl.com.itsense.analysis.event.log.configuration.TermConf");
        digester.addObjectCreate("config/sequence/term/var",        "pl.com.itsense.analysis.event.log.configuration.VarConf");
        digester.addObjectCreate("config/sequence/term/condition",  "pl.com.itsense.analysis.event.log.configuration.ConditionConf");
        digester.addObjectCreate("config/sequenceconsumer",         "pl.com.itsense.analysis.event.log.configuration.SequenceConsumerConf");
        digester.addObjectCreate("config/sequenceconsumer/property","pl.com.itsense.analysis.event.log.configuration.PropertyConf");

        digester.addSetNext("config/event",                     "add",          "pl.com.itsense.analysis.event.configuration.log.EventConf");
        digester.addSetNext("config/event/pattern",             "add",          "pl.com.itsense.analysis.event.configuration.log.PatternConf");
        digester.addSetNext("config/file",                      "add",          "pl.com.itsense.analysis.event.configuration.log.FileConf");
        digester.addSetNext("config/consumer",                  "add",          "pl.com.itsense.analysis.event.configuration.log.EventConsumerConf");
        digester.addSetNext("config/consumer/property",         "add",          "pl.com.itsense.analysis.event.configuration.log.PropertyConf");
        digester.addSetNext("config/sequence",                  "add",          "pl.com.itsense.analysis.event.configuration.log.SequenceConf");
        digester.addSetNext("config/sequence/term",             "add",          "pl.com.itsense.analysis.event.configuration.log.TermConf");
        digester.addSetNext("config/sequence/term/var",         "add",          "pl.com.itsense.analysis.event.configuration.log.VarConf");
        digester.addSetNext("config/sequence/term/condition",   "setCondition", "pl.com.itsense.analysis.event.configuration.log.ConditionConf");
        digester.addSetNext("config/sequenceconsumer",          "add",          "pl.com.itsense.analysis.event.log.configuration.SequenceConsumerConf");
        digester.addSetNext("config/sequenceconsumer/property", "add",          "pl.com.itsense.analysis.event.log.configuration.PropertyConf");
        
        digester.addSetProperties("config");
        digester.addSetProperties("config/event");
        digester.addSetProperties("config/file");
        digester.addSetProperties("config/consumer");
        digester.addSetProperties("config/consumer/property");
        digester.addSetProperties("config/event/pattern");
        digester.addSetProperties("config/sequence");
        digester.addSetProperties("config/sequence/term");
        digester.addSetProperties("config/sequence/term/var");
        digester.addSetProperties("config/sequence/term/condition");
        digester.addSetProperties("config/sequenceconsumer");
        digester.addSetProperties("config/sequenceconsumer/property");
        
        digester.addCallMethod( "config/event/pattern", "setValue", 0);
        

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
    /**
    *
    */
   public void add(final SequenceConf sequence)
   {
       if (!sequences.contains(sequence) && (sequence != null))
       {
           sequences.add(sequence);
       }
   }
   /**
   *
   */
  public void add(final SequenceConsumerConf consumer)
  {
      if (!sequenceConsumers.contains(consumer) && (consumer != null))
      {
          sequenceConsumers.add(consumer);
      }
  }

    /**
     * 
     * @return
     */
    public ArrayList<SequenceConf> getSequences()
    {
        return sequences;
    }
    /**
     * 
     * @return
     */
    public ArrayList<EventConsumerConf> getEventConsumers()
    {
        return eventConsumers;
    }
    
    /**
     * 
     * @return
     */
    public ArrayList<SequenceConsumerConf> getSequenceConsumers()
    {
        return sequenceConsumers;
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
        sb.append("-------- Configuration: Sequences --------").append("\n");
        for (final SequenceConf sequence : sequences)
        {
            sb.append(sequence).append("\n");
        }
        sb.append("-------- Configuration: SequenceConsumers --------").append("\n");
        for (final SequenceConsumerConf consumer: sequenceConsumers)
        {
            sb.append(consumer).append("\n");
        }

        return sb.toString();
    }

}
