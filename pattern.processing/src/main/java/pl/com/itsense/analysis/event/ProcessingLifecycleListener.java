package pl.com.itsense.analysis.event;

import pl.com.itsense.analysis.event.EEngine.ProcessingLifecycle;

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
	void enter(ProcessingLifecycle lifecycle);
}
