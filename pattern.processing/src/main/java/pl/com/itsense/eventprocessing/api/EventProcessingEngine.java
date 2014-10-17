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
     */
    void add(EventProvider provider);
    /**
     * 
     */
    void run();
    /**
     * 
     * @return
     */
    EventProvider[] getProviders();
}
