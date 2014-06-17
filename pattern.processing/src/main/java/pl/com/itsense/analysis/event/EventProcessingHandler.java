package pl.com.itsense.analysis.event;


/**
 * 
 * @author ppretki
 *
 */
public interface EventProcessingHandler extends PropertyHolder 
{
	/** */
	void processEvent(Event event, EEngine engine);
}
