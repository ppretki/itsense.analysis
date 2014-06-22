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
	/** */
	private OpenActionHandlerConf openActionHandlerConf;
	/** */
	private CloseActionHandlerConf closeActionHandlerConf;
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
	 * @param closeActionHandlerConf
	 */
	public void setCloseActionHandlerConf(final CloseActionHandlerConf closeActionHandlerConf) 
	{
		this.closeActionHandlerConf = closeActionHandlerConf;
	}
	
	/**
	 * 
	 * @return
	 */
	public CloseActionHandlerConf getCloseActionHandlerConf() 
	{
		return closeActionHandlerConf;
	}
	/**
	 * 
	 * @param openActionHandlerConf
	 */
	public void setOpenActionHandlerConf(final OpenActionHandlerConf openActionHandlerConf) 
	{
		this.openActionHandlerConf = openActionHandlerConf;
	}
	/**
	 * 
	 * @return
	 */
	public OpenActionHandlerConf getOpenActionHandlerConf() 
	{
		return openActionHandlerConf;
	}
	/**
	 * 
	 */
	@Override
	public String toString() 
	{
		return "Action: id = " + id + ", open = " + open + ", close = " + close + "\n" + (openActionHandlerConf == null ? "" : openActionHandlerConf) + "\n" + (closeActionHandlerConf == null ? "" : closeActionHandlerConf);
	}
	
}
