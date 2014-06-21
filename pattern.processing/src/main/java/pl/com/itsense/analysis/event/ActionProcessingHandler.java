package pl.com.itsense.analysis.event;


/**
 * 
 * @author ppretki
 *
 */
public interface ActionProcessingHandler extends PropertyHolder 
{
	/** */
	void processAction(Action action, EEngine engine);
}
