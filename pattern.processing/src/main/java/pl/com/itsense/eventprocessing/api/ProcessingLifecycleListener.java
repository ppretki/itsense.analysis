package pl.com.itsense.eventprocessing.api;

/**
 * 
 * @author ppretki
 *
 */
public interface ProcessingLifecycleListener 
{
	/**
	 * 
	 */
	void enter(ProcessingLifecycle lifecycle,EEngine eEngine);
}
