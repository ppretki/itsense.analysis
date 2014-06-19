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
	
	
    @Override
    public String toString() 
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("Handler: type = " + type).append("\n");
        for (final PropertyConf property : properties)
        {
            sb.append(property).append("\n");
        }
        return sb.toString();
    }
}
