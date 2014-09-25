package pl.com.itsense.eventprocessing.api;

/**
 *
 */
public interface Report extends PropertyHolder, ProcessingLifecycleListener
{
	void create(EventProcessingEngine engine);
}
