package pl.com.itsense.analysis.event.log.configuration;

/**
 * 
 * @author ppretki
 *
 */
public class PropertyConf 
{
	/** */
	private String value;
	/** */
	private String name;
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
	 * @param name
	 */
	public void setName(final String name) 
	{
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() 
	{
		return name;
	}
	/**
	 * 
	 */
	@Override
	public String toString() 
	{
	return "Property: name = " + name + ", value = " + value;
	}
}