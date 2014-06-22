package pl.com.itsense.analysis.event;

public interface Action extends PropertyHolder
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
