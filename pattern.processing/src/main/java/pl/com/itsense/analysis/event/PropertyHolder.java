package pl.com.itsense.analysis.event;

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
}
