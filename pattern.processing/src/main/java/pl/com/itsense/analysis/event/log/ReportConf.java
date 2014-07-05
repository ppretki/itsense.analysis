package pl.com.itsense.analysis.event.log;

import java.util.ArrayList;

/**
 * 
 * @author ppretki
 *
 */
public class ReportConf extends PropertiesConf
{
	/** */
	private String type;
	
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
	public String getType() 
	{
		return type;
	}

    
	@Override
    public String toString() 
    {
		final StringBuffer sb = new StringBuffer();
        sb.append("Report: type = " + type).append("\n");
        for (final PropertyConf property : getProperties())
        {
        	sb.append(property).append("\n");
        }
        return sb.toString();
    }
}
