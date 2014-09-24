package pl.com.itsense.eventprocessing.api;
/**
 *
 */
public interface ProgressEvent
{
    /**
     * 
     * @return
     */
    double getProgress();
    /**
     * 
     * @return
     */
    ProgressProvider getSource();
}
