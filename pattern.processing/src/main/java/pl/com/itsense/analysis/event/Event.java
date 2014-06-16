package pl.com.itsense.analysis.event;

public interface Event
{
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
	/**
	 * 
	 */
	Object getData();
}
