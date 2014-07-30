package pl.com.itsense.analysis.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
    private HashMap<String, LinkedList<Sequence>> sequances = new HashMap<String, LinkedList<Sequence>>();
    /** */
    private HashMap<EEngine.LogLevel, ArrayList<String>> logs = new HashMap<EEngine.LogLevel, ArrayList<String>>();
    /** */
    private LinkedList<ProcessingLifecycleListener> lifecycleListeners = new LinkedList<ProcessingLifecycleListener>();
    /** */
    private HashMap<String,LinkedList<SequenceConsumer>> sequenceConsumers = new HashMap<String,LinkedList<SequenceConsumer>>();
    /** */
    private SequenceFactory sequenceFactory;
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
    		listener.enter(lifecycle, this);
    	}
    }

    /**
     * 
     */
    private void process(final Event event)
    {
        if (sequenceFactory != null)
        {
            System.out.println("Processing event = " + event.getId());
            final LinkedList<Sequence> openedSequenceQueue = sequances.get(event.getId());
            if (openedSequenceQueue != null)
            {
                final ArrayList<Sequence> closed = new ArrayList<Sequence>(); 
                for (final Sequence seqence : openedSequenceQueue)
                {
                    if (seqence.accept(event))
                    {
                        final String acceptedEventId = seqence.acceptedEventId();
                        if (acceptedEventId == null)
                        {
                            closed.add(seqence);
                        }
                        else
                        {
                            LinkedList<Sequence> q = sequances.get(acceptedEventId);
                            if (q == null)
                            {
                                q = new LinkedList<Sequence>();
                                sequances.put(acceptedEventId , q);
                            }
                            q.add(seqence);
                        }
                    }
                }
                openedSequenceQueue.removeAll(closed);
                notifySequenceConsumers(closed);
            }
            final List<Sequence> list = sequenceFactory.getSequance(event);
            if (list != null)
            {
                for (final Sequence sequence : list)
                {
                    queue = sequances.get(sequence.acceptedEventId());
                    if (queue == null)
                    {
                        queue = new LinkedList<Sequence>();
                        sequances.put(sequence.acceptedEventId(), queue);
                    }
                    queue.add(sequence);
                }
            }
            
        }
        notifyEventConsumers(event);
    }
    /** */
    private void notifySequenceConsumers(final ArrayList<Sequence> closed)
    {
        for (final Sequence sequence : closed)
        {
            final LinkedList<SequenceConsumer> consumers = sequenceConsumers.get(sequence.getId());
            if (consumers != null)
            {
                for (final SequenceConsumer consumer : consumers)
                {
                    consumer.consume(sequence);
                }
            }
        }
    }
    /** */
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
        
        if (consumer instanceof ProcessingLifecycleListener)
        {
            add((ProcessingLifecycleListener)consumer);
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
	/**
	 * 
	 * @param sequenceFactory
	 */
	public void setSequenceFactory(final SequenceFactory sequenceFactory)
    {
        this.sequenceFactory = sequenceFactory;
    }
	/**
	 * 
	 */
    @Override
    public void add(final SequenceConsumer consumer)
    {
        for (final String sequenceId : consumer.getSequenceIds())
        {
            LinkedList<SequenceConsumer> consumerList = sequenceConsumers.get(sequenceId);
            if (consumerList == null)
            {
                consumerList = new LinkedList<SequenceConsumer>();
                sequenceConsumers.put(sequenceId, consumerList);
            }
            if (!consumerList.contains(consumer))
            {
                consumerList.add(consumer);
            }
        }
        if (consumer instanceof ProcessingLifecycleListener)
        {
            add((ProcessingLifecycleListener)consumer);
        }
    }
}
