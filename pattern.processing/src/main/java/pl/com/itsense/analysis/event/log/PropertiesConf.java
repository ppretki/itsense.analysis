package pl.com.itsense.analysis.event.log;

import java.util.ArrayList;

/**
 * 
 * @author ppretki
 *
 */
public class PropertiesConf 
{
    /** */
    private ArrayList<PropertyConf> properties = new ArrayList<PropertyConf>();

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
     * @param properties
     */
    public void setProperties(final ArrayList<PropertyConf> properties) 
    {
		this.properties = properties;
	}
    /**
     * 
     * @param property
     */
    public void addProperty(final PropertyConf property)
    {
    	if (!properties.contains(property))
    	{
    		properties.add(property);
    	}
    }
    
}
