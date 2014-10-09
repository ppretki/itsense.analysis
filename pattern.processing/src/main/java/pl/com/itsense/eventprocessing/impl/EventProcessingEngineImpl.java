package pl.com.itsense.eventprocessing.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import pl.com.itsense.eventprocessing.ProgressProviderImpl;
import pl.com.itsense.eventprocessing.api.Event;
import pl.com.itsense.eventprocessing.api.EventConsumer;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.EventProvider;
import pl.com.itsense.eventprocessing.api.Granularity;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycle;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycleListener;
import pl.com.itsense.eventprocessing.api.ProgressEvent;
import pl.com.itsense.eventprocessing.api.ProgressListener;
import pl.com.itsense.eventprocessing.api.ProgressProvider;
import pl.com.itsense.eventprocessing.provider.RExpressionProvider;
import pl.com.itsense.eventprocessing.xml.EventConf;
import pl.com.itsense.eventprocessing.xml.EventConsumerConf;
import pl.com.itsense.eventprocessing.xml.PropertyConf;

/**
 * 
 */
public class EventProcessingEngineImpl extends ProgressProviderImpl implements EventProcessingEngine, ProgressListener
{
    /** */
    private List<EventProvider> providers;
    /** */
    private HashMap<String, LinkedList<EventConsumer>> consumers = new HashMap<String, LinkedList<EventConsumer>>();
    /** */
    private LinkedList<ProcessingLifecycleListener> lifecycleListeners = new LinkedList<ProcessingLifecycleListener>();

    /**
     * 
     */
    private void init()
    {
        int lineCounter = Integer.MAX_VALUE;
        try
        {
            final String top = file.getTop();
            if (top != null)
            {
                lineCounter = Integer.parseInt(top.trim());
            }
        }
        catch (NumberFormatException e)
        {
            lineCounter = Integer.MAX_VALUE;
        }
        provider = new RExpressionProvider(new File(file.getPath()), configuration.getEvents().toArray(
            new EventConf[0]), lineCounter);

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
                    add(consumerInstance);
                }
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @param provider
     */
    public void execute()
    {
        final Iterator<Event> iterator = provider.iterator();
        if (provider instanceof ProgressProvider)
        {
            ((ProgressProvider) provider).add(this, Granularity.PERCENT);

        }
        enterLifecycle(ProcessingLifecycle.START);
        while (true)
        {
            final Event currentEvents = iterator.next();
            if (currentEvents == null)
            {
                break;
            }
            else
            {
                notifyEventConsumers(currentEvents);
                lastEvent = currentEvents;
                events.add(lastEvent);
            }
        }
        enterLifecycle(ProcessingLifecycle.FINISH);
    }

    /**
     * 
     * @param lifecycle
     */
    private void enterLifecycle(final ProcessingLifecycle lifecycle)
    {
        for (final ProcessingLifecycleListener listener : lifecycleListeners)
        {
            listener.enter(lifecycle, this);
        }
    }

    /**
     * 
     * @param event
     */
    private void notifyEventConsumers(final Event event)
    {
        final LinkedList<EventConsumer> consumerLists = consumers.get(event.getId());
        if (consumerLists != null)
        {
            for (final EventConsumer consumer : consumers.get(event.getId()))
            {
                consumer.process(event);
            }
        }
    }

    @Override
    public void add(final ProcessingLifecycleListener listener)
    {
        if (!lifecycleListeners.contains(listener))
        {
            lifecycleListeners.add(listener);
        }
    }

    @Override
    public void change(final ProgressEvent event)
    {
        progressChanged(event.getProgress());
    }

}
