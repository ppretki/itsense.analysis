package pl.com.itsense.eventprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pl.com.itsense.eventprocessing.api.Configuration;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.Event;
import pl.com.itsense.eventprocessing.api.EventConsumer;
import pl.com.itsense.eventprocessing.api.EventProvider;
import pl.com.itsense.eventprocessing.api.Granularity;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycle;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycleListener;
import pl.com.itsense.eventprocessing.api.ProgressEvent;
import pl.com.itsense.eventprocessing.api.ProgressListener;
import pl.com.itsense.eventprocessing.api.ProgressProvider;
import pl.com.itsense.eventprocessing.api.Report;
import pl.com.itsense.eventprocessing.api.Sequence;
import pl.com.itsense.eventprocessing.api.SequenceConsumer;
import pl.com.itsense.eventprocessing.configuration.EventConf;
import pl.com.itsense.eventprocessing.configuration.EventConsumerConf;
import pl.com.itsense.eventprocessing.configuration.FileConf;
import pl.com.itsense.eventprocessing.configuration.PropertyConf;
import pl.com.itsense.eventprocessing.configuration.ReportConf;
import pl.com.itsense.eventprocessing.configuration.SequenceConsumerConf;
import pl.com.itsense.eventprocessing.provider.TextFileEventProvider;

/**
 * 
 */
public class EventProcessingEngineImpl extends ProgressProviderImpl implements EventProcessingEngine, ProgressListener
{
    /** */
    private EventProvider provider;
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
    private LinkedList<ProcessingLifecycleListener> lifecycleListeners = new LinkedList<ProcessingLifecycleListener>();
    /** */
    private HashMap<String,LinkedList<SequenceConsumer>> sequenceConsumers = new HashMap<String,LinkedList<SequenceConsumer>>();
    /** */
    private SequenceFactory sequenceFactory;
    /** */
    private final Configuration configuration;
    /**
     * 
     */
    public EventProcessingEngineImpl(final Configuration configuration)
    {
        this.configuration = configuration;
        if (configuration != null)
        {
            init();
        }
    }
    /**
     * 
     */
    private void init()
    {
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
        provider = new TextFileEventProvider(new File(file.getPath()), configuration.getEvents().toArray(new EventConf[0]), lineCounter);

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
                    add(consumerInstance);
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
                    add(reportInstance);
                }
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        
        // SEQUENCE FACTORY
        sequenceFactory = new SequenceFactory();
        sequenceFactory.setSequances(configuration.getSequences());
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
    
    /**
     * 
     * @param closed
     */
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
    
    /**
     * 
     * @param sequence
     */
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
