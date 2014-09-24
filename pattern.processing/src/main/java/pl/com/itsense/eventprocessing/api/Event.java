package pl.com.itsense.eventprocessing.api;

/**
 * 
 * @author ppretki
 *
 */
public interface Event  extends PropertyHolder
{
   String PROPERTY_LINE = "LINE"; 
	/**
	 * 
	 * @return
	 */
	long getTimestamp();
	/**
	 * 
	 * @return
	 */
	String getId();
}
