package pl.com.itsense.analysis.event;
/**
 * 
 * @author ppretki
 *
 */
public interface StatefulEventProcessor extends EventProcessor
{
    /**
     * 
     * @param event
     * @param state
     * @param engine
     * @return
     */
    String process(Event event, String state, EEngine engine);
}
