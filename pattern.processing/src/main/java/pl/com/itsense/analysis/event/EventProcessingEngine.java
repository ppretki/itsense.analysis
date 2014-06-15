package pl.com.itsense.analysis.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

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
				events.get(event.getId()).add(event);
				lastEvents.put(event.getId(), event);
				currentEvents[eventIndex] = null;
				for (final EventProcessingHandler handler : handlers)
				{
					handler.processEvent(event, this);
				}
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
	 * @author ppretki
	 *
	 */
	private class EventWrapper implements Event
	{
		/** */
		private final Event event;
		/** */
		private EventWrapper next;
		/** */
		public EventWrapper(final Event event) 
		{
			this.event = event;
		}
		/**
		 * 
		 */
		public long getTimestamp() 
		{
			return event.getTimestamp();
		}
		/**
		 * 
		 */
		public String getId() 
		{
			return event.getId();
		}
		
	}
}
