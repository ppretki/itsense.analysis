package pl.com.itsense.analysis.event;
/**
 * 
 * @author ppretki
 *
 */
public interface StatefulEventProcessor extends EventProcessor
{
	State getState();
}
