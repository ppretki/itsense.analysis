package pl.com.itsense.analysis.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author ppretki
 * 
 */
public class EventProcessingEngine extends ProgressProviderImpl implements EEngine, ProgressListener
{
    /** */
    private final LinkedList<Event> events = new LinkedList<Event>();
    /** */
    private Event lastEvent;
    /** */
    private final LinkedList<Report> reports = new LinkedList<Report>();
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
    public void process(final EventProvider provider)
    {
        final Iterator<Event> iterator = provider.iterator();
        if (provider instanceof ProgressProvider)
        {
            ((ProgressProvider)provider).add(this, Granularity.PERCENT);
            
        }
        enterLifecycle(ProcessingLifecycle.START);
        while (true)
        {
            final Event currentEvents = iterator.next();
            if (currentEvents  == null)
            {
                break;
            }
            else
            {
                process(currentEvents);
                lastEvent = currentEvents ;
                events.add(lastEvent);
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
                            addToQueue(seqence);
                        }
                    }
                }
                openedSequenceQueue.removeAll(closed);
                notifySequenceConsumers(closed);
            }
            final List<Sequence> list = sequenceFactory.getSequance(event);
            if (list != null)
            {
                final ArrayList<Sequence> closed = new ArrayList<Sequence>();
                for (final Sequence sequence : list)
                {
                    final String acceptedEventId = sequence.acceptedEventId();
                    if (acceptedEventId == null)
                    {
                        closed.add(sequence);
                    }
                    else
                    {
                        addToQueue(sequence);
                    }
                }
                notifySequenceConsumers(closed);
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
            
            final LinkedList<SequenceConsumer> allEventsConsumers = sequenceConsumers.get(SequenceConsumer.ALL_EVENTS_CONSUMER);
            if (allEventsConsumers != null)
            {
                for (final SequenceConsumer consumer : allEventsConsumers)
                {
                    consumer.consume(sequence);
                }
            }
            
        }
    }
    
    /** */
    private void addToQueue(final Sequence sequence)
    {
        final String queueName = sequence.acceptedEventId();
        LinkedList<Sequence> queue = sequances.get(queueName);
        if (queue == null)
        {
            queue = new LinkedList<Sequence>();
            sequances.put(queueName , queue);
        }
        queue.add(sequence);
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

    
    private boolean isAllEventConsumer(final SequenceConsumer consumer)
    {
        boolean result = false;
        for (final String sequenceId : consumer.getSequenceIds())
        {
            if (SequenceConsumer.ALL_EVENTS_CONSUMER.equals(sequenceId))
            {
                result = true;
                break;
            }
        }
        return result;
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
        if (isAllEventConsumer(consumer))
        {
            LinkedList<SequenceConsumer> consumerList = sequenceConsumers.get(SequenceConsumer.ALL_EVENTS_CONSUMER);
            if (consumerList == null)
            {
                consumerList = new LinkedList<SequenceConsumer>();
                sequenceConsumers.put(SequenceConsumer.ALL_EVENTS_CONSUMER, consumerList);
            }
            if (!consumerList.contains(consumer))
            {
                consumerList.add(consumer);
            }
        }
        else
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
        }
        if (consumer instanceof ProcessingLifecycleListener)
        {
            add((ProcessingLifecycleListener)consumer);
        }
    }

    /**
     * 
     */
    @Override
    public SequenceConsumer[] getSequenceConsumers()
    {
        final HashSet<SequenceConsumer> seqConsumers = new HashSet<SequenceConsumer>();
        
        for (final List<SequenceConsumer> consumers :  sequenceConsumers.values())
        {
            seqConsumers.addAll(consumers);
        }
        return seqConsumers.toArray(new SequenceConsumer[0]);
    }
    /**
     * 
     */
    @Override
    public void change(final ProgressEvent event)
    {
        progressChanged(event.getProgress());
    }

    @Override
    public void add(final Report report)
    {
        if (!reports.contains(report))
        {
            reports.add(report);
            lifecycleListeners.add(report);
        }
    }
}
