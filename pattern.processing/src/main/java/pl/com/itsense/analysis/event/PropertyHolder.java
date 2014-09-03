package pl.com.itsense.analysis.event;

import java.util.Set;

/**
 * 
 * @author ppretki
 *
 */
public interface PropertyHolder 
{
	/** */
	void setProperty(String name, String value);
	/** */
	String getProperty(String name);
    /** */
    String getProperty(String name, String defaultValue);
	/** */
	Set<String> getProperties();
}
