package pl.com.itsense.analysis.event;

/**
 * 
 * @author ppretki
 *
 */
public interface EEngine 
{
	enum ProcessingLifecycle
	{
		START,
		FINISH
	}
	/**
	 * 
	 * @author ppretki
	 *
	 */
	enum LogLevel
	{
		INFO,
		WARNING,
		ERROR
	}
	/**
	 * 
	 * @param msg
	 * @param level
	 */
	void log(String msg, LogLevel level);
	/**
	 * 
	 * @param listener
	 */
	void add(ProcessingLifecycleListener listener);
	/**
	 * 
	 * @param consumer
	 */
	void add(EventConsumer consumer);
	/**
	 * 
	 * @param consumer
	 */
	void add(SequenceConsumer consumer);
}
