package pl.com.itsense.analysis.event;

/**
 * 
 * @author P.Pretki
 *
 */
public interface Transition 
{
    /**
     * 
     * @return
     */
    State from();
    /**
     * 
     * @return
     */
    State to();
}
