package pl.com.itsense.analysis.event;

import java.util.HashMap;

public class ActionImpl extends PropertyHolderImpl implements Action 
{
	/** */
	private final HashMap<Action.Status,Event> events = new HashMap<Action.Status,Event>();
	/** */
	private Status status;
	/** */
	private final String id;
	
	/**
	 * 
	 */
	public ActionImpl(final String id) 
	{
		this.id = id;
	}

	/**
	 * 
	 */
	@Override
	public Status getStatus() 
	{
		return status;
	}

	/**
	 * 
	 */
	@Override
	public String getId() 
	{
		return id;
	}

	/**
	 * 
	 */
	@Override
	public Event getEvent(Status status) 
	{
		return events.get(status);
	}

	/**
	 * 
	 * @param status
	 * @param event
	 */
	void setStatus(final Status status, Event event)
	{
		this.status = status;
		events.put(status, event);
	}
	
	@Override
	public String toString() 
	{
		final StringBuffer sb = new StringBuffer();
		sb.append("Action: " + id).append("\n");
		sb.append("Status: " + status).append("\n");
		for (final Status status : events.keySet())
		{
			sb.append(status + " : " + events.get(status)).append("\n");
		}
		return sb.toString();
	}
	
	
}
