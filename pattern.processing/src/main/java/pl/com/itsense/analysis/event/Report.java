package pl.com.itsense.analysis.event;

/**
 * 
 * @author ppretki
 *
 */
public interface Report extends PropertyHolder, ProcessingLifecycleListener
{
	void create(EEngine engine);
}
