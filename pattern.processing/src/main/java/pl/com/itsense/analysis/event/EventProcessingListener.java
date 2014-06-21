package pl.com.itsense.analysis.event;

/**
 * 
 * @author ppretki
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
