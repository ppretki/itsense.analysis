package pl.com.itsense.analysis.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class EventProcessingEngine implements EEngine
{
	/** */
	private final LinkedHashMap<String,LinkedList<Event>> events = new LinkedHashMap<String,LinkedList<Event>>();
	/** */
	private final LinkedHashMap<String,Event> lastEvents = new LinkedHashMap<String,Event>();
	/** */
	private String[] eventIds;
	/** */
	private ArrayList<EventProcessingHandler> handlers = new ArrayList<EventProcessingHandler>(); 
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
				for (final EventProcessingHandler handler : handlers)
				{
					handler.processEvent(event, this);
				}
				events.get(event.getId()).add(event);
				lastEvents.put(event.getId(), event);
				currentEvents[eventIndex] = null;
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
	    if (!handlers.contains(handler))
	    {
	        handlers.add(handler);
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
	public List<EventProcessingHandler> getHandlers() 
	{
		return handlers;
	}
}
