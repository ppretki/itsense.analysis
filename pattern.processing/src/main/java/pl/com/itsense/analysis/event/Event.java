package pl.com.itsense.analysis.event;

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
