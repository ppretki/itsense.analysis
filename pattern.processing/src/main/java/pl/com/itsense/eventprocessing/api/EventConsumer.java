package pl.com.itsense.eventprocessing.api;
/**
 *
 */
public interface EventConsumer extends PropertyHolder
{
	/** */
	void process(Event event);
	/** */
	boolean isConsumed(String eventId);
}
