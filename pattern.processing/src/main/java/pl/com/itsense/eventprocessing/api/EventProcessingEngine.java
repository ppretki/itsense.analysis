package pl.com.itsense.eventprocessing.api;

/**
 * 
 * @author ppretki
 *
 */
public interface EventProcessingEngine 
{
	/**
	 * 
	 * @param listener
	 */
	void add(ProcessingLifecycleListener listener);
	/**
	 * 
	 * @param consumer
	 */
	void add(EventConsumer consumer);
    /**
     * 
     * @param consumer
     */
    void add(Report report);
	/**
	 * 
	 * @param consumer
	 */
	void add(SequenceConsumer consumer);
	/**
	 * 
	 */
	SequenceConsumer[] getSequenceConsumers();
}
