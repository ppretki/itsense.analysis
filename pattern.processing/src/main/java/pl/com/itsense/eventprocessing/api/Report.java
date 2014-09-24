package pl.com.itsense.eventprocessing.api;

/**
 * 
 * @author ppretki
 *
 */
public interface Report extends PropertyHolder, ProcessingLifecycleListener
{
	void create(EventProcessingEngine engine);
}
