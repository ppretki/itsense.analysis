package pl.com.itsense.analysis.event;

import java.util.HashMap;
import java.util.Set;

public class PropertyHolderImpl implements PropertyHolder 
{
	/** */
	private HashMap<String,String> properties = new HashMap<String,String>();
	/**
	 * 
	 */
	@Override
	public void setProperty(final String name, final String value) 
	{
	    properties.put(name, value);
	}
	
	/**
	 * 
	 */
	@Override
	public String getProperty(final String name, final String defaultValue)
	{
        final String value = getProperty(name);
        return value != null ?  value : defaultValue;
	}

	/**
	 * 
	 */
	@Override
	public String getProperty(final String name) 
	{
		return properties.get(name);
	}

	
	/**
	 * 
	 */
	@Override
	public Set<String> getProperties() 
	{
		return properties.keySet();
	}	
	
	@Override
	public String toString() 
	{
		final StringBuffer sb = new StringBuffer();
		for (final String name : properties.keySet())
		{
			sb.append(name + " = " + properties.get(name)).append("\n");
		}
		return "Properties: " + sb.toString();
	}
	
	
}
