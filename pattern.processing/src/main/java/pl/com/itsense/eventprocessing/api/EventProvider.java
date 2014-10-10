package pl.com.itsense.eventprocessing.api;

/**
 * 
 */
public interface EventProvider 
{
    /**
     * 
     * @param wait
     * @return
     */
    Event next(long wait);
    /**
     * 
     * @return
     */
    <T extends Event> Class<T>[] getEventClasses();
}
