package pl.com.itsense.analysis.event;

/**
 * 
 * @author P.Pretki
 *
 */
public interface State 
{
    String ID_DEACTIVATE = "DEACTIVATE";
    /**
     * 
     * @return
     */
    long activationTimestamp();
    /**
     * 
     * @return
     */
    String getId();
}
