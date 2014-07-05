package pl.com.itsense.analysis.event;
/**
 * 
 * @author ppretki
 *
 */
public interface EventProcessor extends PropertyHolder
{
	/**
	 * 
	 */
	void process(Event event, EEngine engine);
}
