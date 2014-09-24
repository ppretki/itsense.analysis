package pl.com.itsense.analysis.event.log;

import java.io.File;

import pl.com.itsense.analysis.event.EventProcessingEngine;
import pl.com.itsense.analysis.event.SequenceFactory;
import pl.com.itsense.analysis.event.log.configuration.Configuration;
import pl.com.itsense.analysis.event.log.configuration.EventConf;
import pl.com.itsense.analysis.event.log.configuration.EventConsumerConf;
import pl.com.itsense.analysis.event.log.configuration.FileConf;
import pl.com.itsense.analysis.event.log.configuration.PropertyConf;
import pl.com.itsense.analysis.event.log.configuration.ReportConf;
import pl.com.itsense.analysis.event.log.configuration.SequenceConsumerConf;
import pl.com.itsense.analysis.event.log.providers.TextFileEventProvider;
import pl.com.itsense.eventprocessing.api.EventConsumer;
import pl.com.itsense.eventprocessing.api.EventProvider;
import pl.com.itsense.eventprocessing.api.Granularity;
import pl.com.itsense.eventprocessing.api.ProgressEvent;
import pl.com.itsense.eventprocessing.api.ProgressListener;
import pl.com.itsense.eventprocessing.api.Report;
import pl.com.itsense.eventprocessing.api.SequenceConsumer;

/**
 *
 */
public class LogAnalysis
{
    /** */
    private final EventProcessingEngine engine = new EventProcessingEngine();
    /** */
    private final Configuration configuration;
    /** */
    private EventProvider eventProvider;
    /**
     * 
     */
    public LogAnalysis(final Configuration configuration)
    {
        this.configuration = configuration;
        init();
    }
    /**
     * 
     */
    public void analyze()
    {
        engine.process(eventProvider); 
    }
    /**
     * 
     */
    private void init()
    {
        // PROVIDERS
        final FileConf file = configuration.getFile();
        int lineCounter = Integer.MAX_VALUE;
        try
        {
            final String top = file.getTop();
            if (top != null)
            {
                lineCounter = Integer.parseInt(top.trim());
            }
        }
        catch(NumberFormatException e)
        {
            lineCounter = Integer.MAX_VALUE;
        }
            
        eventProvider = new TextFileEventProvider(new File(file.getPath()), configuration.getEvents().toArray(new EventConf[0]), lineCounter);
        

        // EVENT CONSUMERS
        for (final EventConsumerConf consumer : configuration.getEventConsumers())
        {
            try
            {
                final Class<?> type = Class.forName(consumer.getType());
                if (EventConsumer.class.isAssignableFrom(type))
                {
                    final EventConsumer consumerInstance = (EventConsumer) type.newInstance();
                    for (final PropertyConf property : consumer.getProperties())
                    {
                        consumerInstance.setProperty(property.getName(), property.getValue());
                    }
                    engine.add(consumerInstance);
                }
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        // SEQUANCE CONSUMERS
        for (final SequenceConsumerConf consumer : configuration.getSequenceConsumers())
        {
            try
            {
                final Class<?> type = Class.forName(consumer.getType());
                if (SequenceConsumer.class.isAssignableFrom(type))
                {
                    final SequenceConsumer consumerInstance = (SequenceConsumer) type.newInstance();
                    for (final PropertyConf property : consumer.getProperties())
                    {
                        consumerInstance.setProperty(property.getName(), property.getValue());
                    }
                    consumerInstance.configure(consumer);
                    engine.add(consumerInstance);
                }
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        // REPORTS
        for (final ReportConf report : configuration.getReports())
        {
            try
            {
                final Class<?> type = Class.forName(report.getType());
                if (Report.class.isAssignableFrom(type))
                {
                    final Report reportInstance = (Report) type.newInstance();
                    for (final PropertyConf property : report.getProperties())
                    {
                        reportInstance.setProperty(property.getName(), property.getValue());
                    }
                    engine.add(reportInstance);
                }
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        
        // SEQUENCE FACTORY
        final SequenceFactory sequenceFactory = new SequenceFactory();
        sequenceFactory.setSequances(configuration.getSequences());
        engine.setSequenceFactory(sequenceFactory);
        
    }
    /** */
    public static void main(final String[] args)
    {
        
        final LogAnalysis logAnalysis = new LogAnalysis(Configuration.parse(new File(args[0])));
        logAnalysis.getEngine().add(new ProgressListener()
        {
            
            @Override
            public void change(ProgressEvent event)
            {
                System.out.println(event.getProgress());
            }
        }, Granularity.PERCENT);
        logAnalysis.analyze();
        
    }
    /**
     * 
     * @return
     */
    public EventProcessingEngine getEngine()
    {
        return engine;
    }
    
    @Override
    public String toString()
    {
        return configuration == null ? "" : configuration.toString();
    }
}
