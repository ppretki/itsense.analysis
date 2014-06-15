package pl.com.itsense.analysis.event;

/**
 * 
 * @author ppretki
 *
 */
public interface EventProcessingHandler 
{
	void processEvent(Event event, EEngine engine);
}
