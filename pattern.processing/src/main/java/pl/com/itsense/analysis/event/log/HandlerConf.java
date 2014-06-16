package pl.com.itsense.analysis.event.log;

import java.util.ArrayList;

/**
 * 
 * @author ppretki
 *
 */
public class HandlerConf 
{
	/** */
	private String type;
	/** */
	private ArrayList<HandlerParamConf> params = new ArrayList<HandlerParamConf>();
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
	
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<HandlerParamConf> getParams() 
	{
		return params;
	}
	
	/**
	 * 
	 * @param params
	 */
	public void setParams(final ArrayList<HandlerParamConf> params) 
	{
		this.params = params;
	}
	
	
	/**
	 * 
	 * @param param
	 */
	public void addParam(final HandlerParamConf param)
	{
		params.add(param);
	}
	
}
