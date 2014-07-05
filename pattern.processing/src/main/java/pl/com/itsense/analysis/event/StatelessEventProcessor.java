package pl.com.itsense.analysis.event;

/**
 * 
 * @author ppretki
 *
 */
public interface StatelessEventProcessor extends EventProcessor 
{
	/**
	 * 
	 */
	void process(Event event, EEngine engine);
}
