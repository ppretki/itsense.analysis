package pl.com.itsense.analysis.event;

public interface Action 
{
	enum Status
	{
		OPENED,
		TERMINATED,
		CLOSED
	}
	/** */
	long getOpen();
	/** */
	long getClose();
	/** */
	Status getStatus();
	/** */
	String getId();
	
}
