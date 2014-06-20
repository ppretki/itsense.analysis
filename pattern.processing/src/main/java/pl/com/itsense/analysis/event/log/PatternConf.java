package pl.com.itsense.analysis.event.log;

/**
 * 
 * @author ppretki
 *
 */
public class PatternConf 
{
	/** */
	private String id;
	/** */
	private String regExp;
	
	/**
	 * 
	 * @param id
	 */
	public void setId(final String id) 
	{
		this.id = id;
	}
	
	public void setRegExp(final String regExp) 
	{
		this.regExp = regExp;
	}
	
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
	 * @return
	 */
	public String getRegExp() 
	{
		return regExp;
	}

	
	/**
	 * 
	 */
	@Override
	public String toString() 
	{
		return "Pattern: id = " + id + ", regExp = " + regExp;
	}
}
