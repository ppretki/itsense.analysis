package pl.com.itsense.eventprocessing.api;

import pl.com.itsense.eventprocessing.api.EEngine;

/**
 *
 */
public interface EventProcessingListener 
{
	/**
	 * 
	 * @param engine
	 */
	void beginProcessing(EEngine engine);
	/**
	 * 
	 * @param engine
	 */
	void endProcessing(EEngine engine);
}
