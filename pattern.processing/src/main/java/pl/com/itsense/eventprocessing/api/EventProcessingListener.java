package pl.com.itsense.eventprocessing.api;

import pl.com.itsense.eventprocessing.api.EventProcessingEngine;

/**
 *
 */
public interface EventProcessingListener 
{
	/**
	 * 
	 * @param engine
	 */
	void beginProcessing(EventProcessingEngine engine);
	/**
	 * 
	 * @param engine
	 */
	void endProcessing(EventProcessingEngine engine);
}
