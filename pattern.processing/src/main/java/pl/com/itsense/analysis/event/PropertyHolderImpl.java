package pl.com.itsense.analysis.event;

import java.util.HashMap;

public class PropertyHolderImpl implements PropertyHolder 
{
	/** */
	private HashMap<String,String> properties = new HashMap<String,String>();
	/**
	 * 
	 */
	@Override
	public void setProperty(final String name,final String value) 
	{
		properties.put(name, value);
	}

	/**
	 * 
	 */
	@Override
	public String getProperty(final String name) 
	{
		return properties.get(name);
	}	
	
	
}
