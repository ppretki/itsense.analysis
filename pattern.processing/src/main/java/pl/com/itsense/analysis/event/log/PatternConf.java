package pl.com.itsense.analysis.event.log;

/**
 * 
 * @author ppretki
 *
 */
public class PatternConf 
{
	/** */
	private String value;
	
	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) 
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
	 */
	@Override
	public String toString() 
	{
		return "Pattern: value = " + value;
	}
}
