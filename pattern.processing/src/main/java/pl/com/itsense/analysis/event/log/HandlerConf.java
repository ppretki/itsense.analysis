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
	private ArrayList<PropertyConf> params = new ArrayList<PropertyConf>();
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
	public ArrayList<PropertyConf> getParams() 
	{
		return params;
	}
	
	/**
	 * 
	 * @param params
	 */
	public void setParams(final ArrayList<PropertyConf> params) 
	{
		this.params = params;
	}
	
	
	/**
	 * 
	 * @param param
	 */
	public void addParam(final PropertyConf param)
	{
		params.add(param);
	}
	
}
