package pl.com.itsense.analysis.event;

public interface Action 
{
	enum Status
	{
		OPEN,
		TERMINATE,
		CLOSE
	}
	/** */
	Status getStatus();
	/** */
	String getId();
	/** */
	Event getEvent(Status status);
}
