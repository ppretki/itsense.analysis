package pl.com.itsense.analysis.event.log;

/**
 * 
 * @author ppretki
 *
 */
public class ProcessorConf 
{
	/**	 */
	private String id;
	/**	 */
	private String type;
	/**	 */
	private String events;
	/**
	 * 
	 * @return
	 */
	public String getId() 
	{
		return id;
	}
	/**
	 * 
	 * @param id
	 */
	public void setId(final String id) 
	{
		this.id = id;
	}
	/**
	 * 
	 * @param events
	 */
	public void setEvents(final String events) 
	{
		this.events = events;
	}
	/**
	 * 
	 * @param type
	 */
	public void setType(final String type) 
	{
		this.type = type;
	}
	/**
	 * 
	 * @return
	 */
	public String getEvents() 
	{
		return events;
	}
	/**
	 * 
	 * @return
	 */
	public String getType() 
	{
		return type;
	}
	/**
	 * 
	 */
	@Override
	public String toString() 
	{
		return "Action: id = " + id + ", type = " + type + ", events = " + events;
	}
	
}
