package pl.com.itsense.analysis.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author ppretki
 * 
 */
public class EventProcessingEngine implements EEngine
{
    /** */
    private final LinkedList<Event> events = new LinkedList<Event>();
    /** */
    private Event lastEvent;
    /** */
    private HashMap<String, LinkedList<EventConsumer>> consumers = new HashMap<String, LinkedList<EventConsumer>>();
    /** */
    private HashMap<EEngine.LogLevel, ArrayList<String>> logs = new HashMap<EEngine.LogLevel, ArrayList<String>>();
    /***/
    private LinkedList<ProcessingLifecycleListener> lifecycleListeners = new LinkedList<ProcessingLifecycleListener>();
    /** */
    public void process(final EventProvider[] providers)
    {
        final Event[] currentEvents = new Event[providers.length];
        final Iterator<Event>[] iterators = new Iterator[providers.length];
        for (int i = 0; i < providers.length; i++)
        {
            iterators[i] = providers[i].iterator();
        }
        enterLifecycle(ProcessingLifecycle.START);
        while (true)
        {
            for (int i = 0; i < currentEvents.length; i++)
            {
                if ((currentEvents[i] == null) && (iterators[i].hasNext()))
                {
                    currentEvents[i] = iterators[i].next();
                }
            }

            Event event = null;
            int eventIndex = -1;
            for (int i = 0; i < currentEvents.length; i++)
            {
                if ((currentEvents[i] != null) && ((event == null) || (event.getTimestamp() > currentEvents[i].getTimestamp())))
                {
                    eventIndex = i;
                    event = currentEvents[i];
                }
            }
            if (event == null)
            {
                break;
            }
            else
            {
                process(event);
                lastEvent = event;
                events.add(lastEvent);
                currentEvents[eventIndex] = null;

            }
        }
        enterLifecycle(ProcessingLifecycle.FINISH);
    }

    /** */
    private void enterLifecycle(final ProcessingLifecycle lifecycle)
    {
    	for (final ProcessingLifecycleListener listener : lifecycleListeners)
    	{
    		listener.enter(lifecycle);
    	}
    }

    /**
     * 
     */
    private void process(final Event event)
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final EventConsumer consumer)
    {
        final String events = consumer.getProperty("events");

        if (events != null && events.length() > 0)
        {
            for (final String eventId : events.split(","))
            {
                final String trimmedEventId = eventId.trim();
                LinkedList<EventConsumer> consumerList = consumers.get(trimmedEventId);
                if (consumerList == null)
                {
                	consumerList = new LinkedList<EventConsumer>();
                	consumers.put(trimmedEventId, consumerList);
                }
                if (!consumerList.contains(consumer))
                {
                	consumerList.add(consumer);
                }
            }
        }
    }

    

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(final String msg, final LogLevel level)
    {
        ArrayList<String> log = logs.get(level);
        if (log == null)
        {
            log = new ArrayList<String>();
            logs.put(level, log);
        }
        log.add(msg);
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public void add(final ProcessingLifecycleListener listener) 
	{
		if (!lifecycleListeners.contains(listener))
		{
			lifecycleListeners.add(listener);
		}
	}
}
