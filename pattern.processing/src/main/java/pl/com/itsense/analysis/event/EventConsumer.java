package pl.com.itsense.analysis.event;
/**
 * 
 * @author ppretki
 *
 */
public interface EventConsumer extends PropertyHolder
{
	/** */
	void process(Event event);
	/** */
	boolean isConsumed(String eventId);
}
