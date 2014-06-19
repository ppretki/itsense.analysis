package pl.com.itsense.analysis.event;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EventProcessingEngine implements EEngine
{
	private static final String ALL = (new BigInteger(128, new Random())).toString(); 
	/** */
	private final LinkedHashMap<String,LinkedList<Event>> events = new LinkedHashMap<String,LinkedList<Event>>();
	/** */
	private final LinkedHashMap<String,Event> lastEvents = new LinkedHashMap<String,Event>();
	/** */
	private String[] eventIds;
	/** */
	private Event lastEvent;
	/** */
	private HashMap<String,List<EventProcessingHandler>> handlers = new HashMap<String,List<EventProcessingHandler>>();
	/** */
	private ArrayList<Report> reports = new ArrayList<Report>();
	/** */
	private HashMap<EEngine.LogLevel, ArrayList<String>> logs = new HashMap<EEngine.LogLevel, ArrayList<String>>();
	/** */
	public void process(final EventProvider[] providers)
	{
		final Event[] currentEvents = new Event[providers.length];
		final Iterator<Event>[] iterators = new Iterator[providers.length];
		final ArrayList<String> eventIdsList = new ArrayList<String>(); 
		for (int i = 0 ;i < providers.length; i++)
		{
			eventIdsList.addAll(Arrays.asList(providers[i].getEventIds()));
			iterators[i] = providers[i].iterator();
		}
		eventIds = eventIdsList.toArray(new String[0]);
		for (final String eventId : eventIds)
		{
			events.put(eventId, new LinkedList<Event>());
		}
		while (true)
		{
			for (int i = 0 ; i < currentEvents.length; i++)
			{
				if ((currentEvents[i] == null) && (iterators[i].hasNext()))
				{
					currentEvents[i] = iterators[i].next();
				}
			}
			
			Event event = null;
			int eventIndex = -1;
			for (int i = 0 ; i < currentEvents.length; i++)
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
				dispatchEvent(event, handlers.get(event.getId()));
				dispatchEvent(event, handlers.get(ALL));
				lastEvent = event;
				events.get(lastEvent.getId()).add(lastEvent);
				lastEvents.put(lastEvent.getId(), lastEvent);
				currentEvents[eventIndex] = null;
			}
		}
		for (final Report report : reports)
		{
		    report.create(this);
		}
	}
	/**
	 * 
	 */
	private void dispatchEvent(final Event event, final List<EventProcessingHandler> dest)
	{
		if ((dest != null) && !dest.isEmpty())
		{
			for (final EventProcessingHandler handler : dest)
			{
				handler.processEvent(event, this);
			}
		}
	}

	/**
	 * 
	 */
	public Event getEvent(final String eventId)
	{
		return lastEvents.get(eventId);
	}

	/**
	 * 
	 */
	public String[] getEventIds() 
	{
		return  eventIds;
	}

	/**
	 * 
	 */
	public LinkedList<Event> getEvents(final String eventId) 
	{
		return events.get(eventId);
	}
	
	
	/**
	 * 
	 * @param handler
	 */
	public void addProcessingHandler(final EventProcessingHandler handler)
	{
		final String values = handler.getProperty("events");
		
		if ((values == null) || (values.length() == 0))
		{
			List<EventProcessingHandler> list = handlers.get(ALL);
			if (list == null)
			{
				list = new ArrayList<EventProcessingHandler>();
				handlers.put(ALL, list);
			}
			if (!list.contains(handler))
			{
				list.add(handler);
			}
		}
		else
		{
			for (final String eventId : values.split(","))
			{
				final String trimmedEventId = eventId.trim();
				List<EventProcessingHandler> list = handlers.get(trimmedEventId);
				if (list == null)
				{
					list = new ArrayList<EventProcessingHandler>();
					handlers.put(trimmedEventId, list);
				}
				if (!list.contains(handler))
				{
					list.add(handler);
				}
				
			}
		}
	}
        /**
         * 
         * @param handler
         */
        public void addReport(final Report report)
        {
            if (!reports.contains(report))
            {
                reports.add(report);
            }
        }

	/**
	 * 
	 */
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
	 * 
	 */
	@Override
	public List<String> getLogs(LogLevel level) 
	{
		return logs.get(level);
	}
	
	/**
	 * 
	 */
	@Override
	public EventProcessingHandler[] getHandlers() 
	{
		final HashSet<EventProcessingHandler> set = new HashSet<EventProcessingHandler>(); 
		for (final String eventId : handlers.keySet())
		{
			set.addAll(handlers.get(eventId));
		}
		return set.toArray(new EventProcessingHandler[0]);
	}


    @Override
    public Report[] getReports() 
    {
        return reports.toArray(new Report[0]);
    }

	@Override
	public Event getEvent() 
	{
		return lastEvent;
	}
}
