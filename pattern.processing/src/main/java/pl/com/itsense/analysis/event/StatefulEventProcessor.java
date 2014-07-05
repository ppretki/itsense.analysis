package pl.com.itsense.analysis.event;
/**
 * 
 * @author ppretki
 *
 */
public interface StatefulEventProcessor extends EventProcessor
{
	String process(Event event, String state, EEngine engine);

}
