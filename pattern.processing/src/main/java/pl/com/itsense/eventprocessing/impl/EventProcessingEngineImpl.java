package pl.com.itsense.eventprocessing.impl;

import java.util.LinkedList;

import pl.com.itsense.eventprocessing.api.Event;
import pl.com.itsense.eventprocessing.api.EventConsumer;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.EventProvider;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycle;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycleListener;
import pl.com.itsense.eventprocessing.api.ProgressEvent;
import pl.com.itsense.eventprocessing.api.ProgressListener;

/**
 * 
 */
public class EventProcessingEngineImpl extends ProgressProviderImpl implements EventProcessingEngine, ProgressListener
{
    /** */
    private LinkedList<EventProvider> providers = new LinkedList<EventProvider>();
    /** */
    private LinkedList<EventConsumer> consumers = new LinkedList<EventConsumer>();
    /** */
    private LinkedList<ProcessingLifecycleListener> lifecycleListeners = new LinkedList<ProcessingLifecycleListener>();

    /**
     * 
     * @param provider
     */
    public void run()
    {
        if (!providers.isEmpty())
        { 
            for (final EventProvider provider : providers)
            {
                provider.init();
            }
            enterLifecycle(ProcessingLifecycle.START);
            final Event[] events = new Event[providers.size()];
            final EventProvider[] eventProviders = providers.toArray(new EventProvider[0]);
            while (true)
            {
                Event event = null;
                int providerIndex = -1;
                for (int i = 0; i < eventProviders.length; i++)
                {
                    if (events[i] == null)
                    {
                        events[i] = eventProviders[i].next(0);
                    }
                    if ((events[i] != null) && ((event == null) || (events[i].getTimestamp() < event.getTimestamp())))
                    {
                        event = events[i];
                        providerIndex = i;
                    }
                }
                
                if (event == null)
                {
                    break;
                }
                else
                {
                    events[providerIndex] = null;
                    notifyEventConsumers(event);
                }
            }
            enterLifecycle(ProcessingLifecycle.FINISH);
        }
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
        for (final EventConsumer consumer : consumers)
        {
            consumer.process(event);
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

    @Override
    public void add(final EventConsumer consumer)
    {
        if (!consumers.contains(consumer))
        {
            consumers.add(consumer);
            if (consumer instanceof ProcessingLifecycleListener)
            {
                add((ProcessingLifecycleListener)consumer);
            }
        }
    
    }
    
    @Override
    public EventProvider[] getProviders()
    {
        return providers.toArray(new EventProvider[0]);
    }

    @Override
    public void add(final EventProvider provider)
    {
        if (!providers.contains(provider))
        {
            providers.add(provider);
        }
    }
}
