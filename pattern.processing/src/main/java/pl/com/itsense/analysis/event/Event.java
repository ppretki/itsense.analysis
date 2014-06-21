package pl.com.itsense.analysis.event;

public interface Event  extends PropertyHolder
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
}
