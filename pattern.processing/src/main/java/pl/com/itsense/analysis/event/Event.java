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
	String getField(String field);
}
