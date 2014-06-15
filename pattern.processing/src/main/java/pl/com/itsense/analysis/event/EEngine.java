package pl.com.itsense.analysis.event;

import java.util.Collection;

public interface EEngine 
{
	/**
	 * 
	 * @param eventId
	 * @return
	 */
	Event getEvent(String eventId);
	/**
	 * 
	 * @return
	 */
	String[] getEventIds();
	/**
	 * 
	 */
	Collection<Event> getEvents(String eventId);
}
