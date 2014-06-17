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
	private ArrayList<PropertyConf> properties = new ArrayList<PropertyConf>();
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
	public ArrayList<PropertyConf> getProperties() 
	{
		return properties;
	}
	
	/**
	 * 
	 * @param params
	 */
	public void setParams(final ArrayList<PropertyConf> properties) 
	{
		this.properties = properties;
	}
	
	
	/**
	 * 
	 * @param param
	 */
	public void addProperty(final PropertyConf property)
	{
		properties.add(property);
	}
	
}
