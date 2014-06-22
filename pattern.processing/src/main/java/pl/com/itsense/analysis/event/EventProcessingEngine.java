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
	private final HashMap<Action.Status,LinkedList<Action>> actions = new HashMap<Action.Status,LinkedList<Action>>();
	/** */
	private Event lastEvent;
	/** */
	private HashMap<String,List<ActionProcessingHandler>> actionHandlers = new HashMap<String,List<ActionProcessingHandler>>();
	/** */
	private HashMap<String,List<EventProcessingHandler>> eventHandlers = new HashMap<String,List<EventProcessingHandler>>();
	/** */
	private HashMap<String,List<String>> openEventActionMap = new HashMap<String,List<String>>();
	/** */
	private HashMap<String,List<String>> closeEventActionMap = new HashMap<String,List<String>>();
	/** */
	private HashMap<String,OpenActionHandler> openActionHandlerMap = new HashMap<String,OpenActionHandler>();
	/** */
	private HashMap<String,CloseActionHandler> closeActionHandlerMap = new HashMap<String,CloseActionHandler>();
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
				closeActions(event);
				openActions(event);
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
		System.out.println(actions.get(Status.TERMINATE));
	}
	/**
	 * 
	 * @param event
	 */
	private void openActions(final Event event)
	{
		//System.out.println("openActions: " + event);
		final List<String> actionIds = openEventActionMap.get(event.getId());
		if (actionIds != null)
		{
			for (final String actionId : actionIds)
			{
				final OpenActionHandler openActionHandler = openActionHandlerMap.get(actionId);
				final Status status;
				if (openActionHandler != null)
				{
					status = openActionHandler.processOpenActionEvent(event, this);
				}
				else
				{
					status = Status.OPEN;
				}
				if (Status.OPEN.equals(status))
				{
					final ActionImpl actionImpl = new ActionImpl(actionId);
					actionImpl.setStatus(Status.OPEN, event);
					addActionToQueue(actionImpl);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private void addActionToQueue(final Action action)
	{
		//System.out.println("addActionToQueue: " + action);
		LinkedList<Action> queue = actions.get(action.getStatus());
		if (queue == null)
		{
			queue = new LinkedList<Action>();
			actions.put(action.getStatus(), queue);
		}
		queue.add(action);
		if (Status.CLOSE.equals(action.getStatus()))
		{
			dispatchAction(action, actionHandlers.get(action.getId()));
		}
	}

	/**
	 * 
	 */
	private void removeActionFromQueue(final Action action)
	{
	}
	
	/**
	 * 
	 */
	private void closeActions(final Event event)
	{
		final List<String> actionIds = closeEventActionMap.get(event.getId());
		if (actionIds != null)
		{
			for (final String actionId : actionIds)
			{
				final LinkedList<Action> queue = actions.get(Status.OPEN);
				if (queue != null)
				{
					final ArrayList<Action> actionsToRemove = new ArrayList<Action>();
					final CloseActionHandler closeActionHandler = closeActionHandlerMap.get(actionId);
					for (final Action action : queue)
					{
						if (action.getId().equals(actionId))
						{
							final Status status;
							if (closeActionHandler != null)
							{
								status = closeActionHandler.processCloseActionEvent(action, event, this);
							}
							else
							{
								status = Status.CLOSE;
							}
							if (Status.CLOSE.equals(status) || Status.TERMINATE.equals(status))
							{
								actionsToRemove.add(action);
								((ActionImpl)action).setStatus(status, event);
								addActionToQueue(action);
							}
						}
					}
					for (final Action action : actionsToRemove) queue.remove(action);
				}
			}
		}
	}

	
	/**
	 * 
	 */
	private void dispatchAction(final Action action, final List<ActionProcessingHandler> dest)
	{
		if ((dest != null) && !dest.isEmpty())
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

		if (eventHandlers.get(ALL) != null)
		{
			for (final EventProcessingHandler handler : eventHandlers.get(ALL))
			{
				handler.processEvent(event, this);
			}
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
	public Event getEvent(String eventId) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 */
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
	
	/**
	 * 
	 */
	public void addAction(final String actionId, final String[] openEventIds, final String[] closeEventIds, final OpenActionHandler openActionHandler, final CloseActionHandler closeActionHandler)
	{
		if (openEventIds != null)
		{
			for (final String openEventId : openEventIds)
			{
				List<String> actionIds = openEventActionMap.get(openEventId);
				if (actionIds == null)
				{
					actionIds = new LinkedList<String>();
					openEventActionMap.put(openEventId, actionIds);
				}
				if (!actionIds.contains(actionId))
				{
					actionIds.add(actionId);
				}
			}
		}
		
		if (closeEventIds != null)
		{
			for (final String closeEventId : closeEventIds)
			{
				List<String> actionIds = closeEventActionMap.get(closeEventId);
				if (actionIds == null)
				{
					actionIds = new LinkedList<String>();
					closeEventActionMap.put(closeEventId, actionIds);
				}
				if (!actionIds.contains(actionId))
				{
					actionIds.add(actionId);
				}
			}
		}
		
		if (openActionHandler != null)
		{
			openActionHandlerMap.put(actionId, openActionHandler);
		}
		
		if (closeActionHandler != null)
		{
			closeActionHandlerMap.put(actionId, closeActionHandler);
		}
		
		
		
		
		
		
		
		
		
	}
	
	
	
}
