package pl.com.itsense.analysis.event.log;

/**
 * 
 * @author ppretki
 *
 */
public class ActionConf 
{
	/**	 */
	private String id;
	/**	 */
	private String open;
	/**	 */
	private String close;
	/**	 */
	private String terminate;
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
	 * @return
	 */
	public String getOpen() 
	{
		return open;
	}
	/**
	 * 
	 * @param open
	 */
	public void setOpen(final String open) 
	{
		this.open = open;
	}
	/**
	 * 
	 * @return
	 */
	public String getClose() 
	{
		return close;
	}
	/**
	 * 
	 * @param close
	 */
	public void setClose(final String close) 
	{
		this.close = close;
	}
	/**
	 * 
	 * @return
	 */
	public String getTerminate() 
	{
		return terminate;
	}
	
	/**
	 * 
	 * @param terminate
	 */
	public void setTerminate(final String terminate) 
	{
		this.terminate = terminate;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() 
	{
		return "Action: id = " + id + ", open = " + open + ", close = " + close + ", terminate = " + terminate;
	}
	
}
