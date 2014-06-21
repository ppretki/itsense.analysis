package pl.com.itsense.analysis.event;

import java.util.Collection;
import java.util.List;

public interface EEngine 
{
	enum LogLevel
	{
		INFO,
		WARNING,
		ERROR
	}
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
	Event getEvent();
	/**
	 * 
	 * @return
	 */
	String[] getEventIds();
	/**
	 * 
	 */
	Collection<Event> getEvents(String eventId);
	/**
	 * 
	 */
	void log(String msg, LogLevel level);
	/**
	 * 
	 */
	List<String> getLogs(LogLevel level);
	/**
	 * 
	 */
	ActionProcessingHandler[] getActionHandlers();
	/**
	 * 
	 */
	EventProcessingHandler[] getEventHandlers();

	/**
	 * 
    */
	Report[] getReports();

}
