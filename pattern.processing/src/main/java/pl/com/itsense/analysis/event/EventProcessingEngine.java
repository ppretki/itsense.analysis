package pl.com.itsense.analysis.event;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.com.itsense.analysis.event.Action.Status;

public class EventProcessingEngine implements EEngine
{
	/** */
	private final String ALL = (new BigInteger(128, new Random())).toString(16);
	/** */
	private final LinkedList<Event> events = new LinkedList<Event>();
	/** */
	private Event lastEvent;
	/** */
	private HashMap<String,List<ActionProcessingHandler>> actionHandlers = new HashMap<String,List<ActionProcessingHandler>>();
	/** */
	private HashMap<String,List<EventProcessingHandler>> eventHandlers = new HashMap<String,List<EventProcessingHandler>>();
	/** */
	private ArrayList<Report> reports = new ArrayList<Report>();
	/** */
	private HashMap<EEngine.LogLevel, ArrayList<String>> logs = new HashMap<EEngine.LogLevel, ArrayList<String>>();
	/** */
	public void process(final EventProvider[] providers)
	{
		final Event[] currentEvents = new Event[providers.length];
		final Iterator<Event>[] iterators = new Iterator[providers.length];
		for (int i = 0 ;i < providers.length; i++)
		{
			iterators[i] = providers[i].iterator();
		}
		
		beginProcessing();
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
				dispatchEvent(event);
				lastEvent = event;
				events.add(lastEvent);
				currentEvents[eventIndex] = null;
			}
		}
		endProcessing();
		for (final Report report : reports)
		{
		    report.create(this);
		}
	}
	/**
	 * 
	 */
	private void dispatchAction(final Action action, final List<ActionProcessingHandler> dest)
	{
		if ((dest != null) && !dest.isEmpty() && (action.getStatus() == Status.CLOSED))
		{
			for (final ActionProcessingHandler handler : dest)
			{
				handler.processAction(action, this);
			}
		}
	}
	
	/**
	 * 
	 */
	private void beginProcessing()
	{
		for (final ActionProcessingHandler handler : getActionHandlers())
		{
			if (handler instanceof EventProcessingListener)
			{
				((EventProcessingListener)handler).beginProcessing(this);
			}
		}

		for (final EventProcessingHandler handler : getEventHandlers())
		{
			if (handler instanceof EventProcessingListener)
			{
				((EventProcessingListener)handler).beginProcessing(this);
			}
		}
	}

	/**
	 * 
	 */
	private void endProcessing()
	{
		for (final ActionProcessingHandler handler : getActionHandlers())
		{
			if (handler instanceof EventProcessingListener)
			{
				((EventProcessingListener)handler).endProcessing(this);
			}
		}

		for (final EventProcessingHandler handler : getEventHandlers())
		{
			if (handler instanceof EventProcessingListener)
			{
				((EventProcessingListener)handler).endProcessing(this);
			}
		}
	}
	
	/**
	 * 
	 */
	private void dispatchEvent(final Event event)
	{
		if (eventHandlers.get(event.getId()) != null)
		{
			for (final EventProcessingHandler handler : eventHandlers.get(event.getId()))
			{
				handler.processEvent(event, this);
			}
		}

		for (final EventProcessingHandler handler : eventHandlers.get(ALL))
		{
			handler.processEvent(event, this);
		}
		
		
	}


	/**
	 * 
	 */
	public String[] getEventIds() 
	{
		return  null;
	}

	/**
	 * 
	 */
	public LinkedList<Event> getEvents(final String eventId) 
	{
		return null;
	}
	
	
	/**
	 * 
	 * @param handler
	 */
	public void addActionProcessingHandler(final ActionProcessingHandler handler)
	{
		final String values;
		if (handler.getProperty("actions")  != null)
		{
			values = handler.getProperty("actions");
		}
		else
		{
			values = ALL;
		}
		if (values.length() > 0)
		{
			for (final String actionId : values.split(","))
			{
				final String trimmedActionId = actionId.trim();
				List<ActionProcessingHandler> list = actionHandlers.get(trimmedActionId);
				if (list == null)
				{
					list = new ArrayList<ActionProcessingHandler>();
					actionHandlers.put(trimmedActionId, list);
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
	public void addEventProcessingHandler(final EventProcessingHandler handler)
	{
		final String values;
		if (handler.getProperty("events")  != null)
		{
			values = handler.getProperty("events");
		}
		else
		{
			values = ALL;
		}

		if (values.length() > 0)
		{
			for (final String actionId : values.split(","))
			{
				final String trimmedActionId = actionId.trim();
				List<EventProcessingHandler> list = eventHandlers.get(trimmedActionId);
				if (list == null)
				{
					list = new ArrayList<EventProcessingHandler>();
					eventHandlers.put(trimmedActionId, list);
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
	public ActionProcessingHandler[] getActionHandlers() 
	{
		final HashSet<ActionProcessingHandler> set = new HashSet<ActionProcessingHandler>(); 
		for (final String eventId : actionHandlers.keySet())
		{
			set.addAll(actionHandlers.get(eventId));
		}
		return set.toArray(new ActionProcessingHandler[0]);
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
	
	@Override
	public Event getEvent(String eventId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public EventProcessingHandler[] getEventHandlers() 
	{
		final HashSet<EventProcessingHandler> set = new HashSet<EventProcessingHandler>(); 
		for (final String eventId : eventHandlers.keySet())
		{
			set.addAll(eventHandlers.get(eventId));
		}
		return set.toArray(new EventProcessingHandler[0]);
	}
	
	
	
	
	
}
