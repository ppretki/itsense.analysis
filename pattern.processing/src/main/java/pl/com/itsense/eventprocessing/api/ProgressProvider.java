package pl.com.itsense.eventprocessing.api;
/**
 * 
 */
public interface ProgressProvider
{
    /**
     * 
     * @param listener
     */
    void add(ProgressListener listener, Granularity granularity);
    /**
     * 
     * @param listener
     */
    void remove(ProgressListener listener);
}
