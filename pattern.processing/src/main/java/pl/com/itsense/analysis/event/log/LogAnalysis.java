package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.util.ArrayList;

import pl.com.itsense.analysis.event.EventConsumer;
import pl.com.itsense.analysis.event.EventProcessingEngine;
import pl.com.itsense.analysis.event.EventProvider;
import pl.com.itsense.analysis.event.SequenceConsumer;
import pl.com.itsense.analysis.event.SequenceFactory;
import pl.com.itsense.analysis.event.log.configuration.Configuration;
import pl.com.itsense.analysis.event.log.configuration.EventConf;
import pl.com.itsense.analysis.event.log.configuration.EventConsumerConf;
import pl.com.itsense.analysis.event.log.configuration.FileConf;
import pl.com.itsense.analysis.event.log.configuration.PropertyConf;
import pl.com.itsense.analysis.event.log.configuration.SequenceConsumerConf;
import pl.com.itsense.analysis.event.log.providers.TextFileEventProvider;

/**
 * 
 * @author ppretki
 *
 */
public class LogAnalysis
{
    /** */
    public static void main(final String[] args)
    {
        final Configuration configuration = Configuration.parse(new File(args[0]));
        System.out.println(configuration);
        final EventProcessingEngine engine = new EventProcessingEngine();

        // PROVIDERS
        final EventProvider[] eventProviders = new EventProvider[configuration.getFiles().size()];
        for (int i = 0; i < eventProviders.length; i++)
        {
            final FileConf file = configuration.getFiles().get(i);
            final ArrayList<EventConf> events = new ArrayList<EventConf>();
            for (final EventConf event : configuration.getEvents())
            {
                if (file.getId().equals(event.getFileid()))
                {
                    events.add(event);
                }
            }
            eventProviders[i] = new TextFileEventProvider(new File(file.getPath()), events.toArray(new EventConf[0]),
                file.getFrom());
        }

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

        // SEQUENCE FACTORY
        final SequenceFactory sequenceFactory = new SequenceFactory();
        sequenceFactory.setSequances(configuration.getSequences());
        engine.setSequenceFactory(sequenceFactory);
        
        engine.process(eventProviders);
    }

}
