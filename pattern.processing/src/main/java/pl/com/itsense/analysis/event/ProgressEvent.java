package pl.com.itsense.analysis.event;

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
