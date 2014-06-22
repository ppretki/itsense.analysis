package pl.com.itsense.analysis.event.log;

/**
 * 
 * @author ppretki
 *
 */
public class CloseActionHandlerConf 
{
	/** */
	private String type;
	
	/** */
	private String value;
	/**
	 * 
	 * @param value
	 */
	public void setValue(final String value) 
	{
		this.value = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getValue() 
	{
		return value;
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
	 * @param type
	 */
	public void setType(final String type) 
	{
		this.type = type;
	}
	
	
	@Override
	public String toString() 
	{
		return "CloseActionHandlerConf: type = " + type + ", value = " + value;
	}

}
