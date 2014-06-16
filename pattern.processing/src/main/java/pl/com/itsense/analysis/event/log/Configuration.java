package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import pl.com.itsense.analysis.event.Event;

/**
 * 
 * @author ppretki
 *
 */
public class Configuration 
{
	/** */
	private final ArrayList<EventConf> events = new ArrayList<EventConf>();
	/** */
	private final ArrayList<FileConf> files = new ArrayList<FileConf>();
	/** */
	private String decimalFormat;
	
	/**
	 * 
	 * @param xmlConfigFile
	 */
	public static final Configuration parse(final File file)
	{
		Configuration configuration = null;
		
		final Digester digester = new Digester();
		digester.setValidating( false );
		digester.addObjectCreate( "config", "pl.com.itsense.analysis.event.log.Configuration" );
		digester.addObjectCreate( "config/event", "pl.com.itsense.analysis.event.log.EventConf" );
		digester.addObjectCreate( "config/file", "pl.com.itsense.analysis.event.log.FileConf" );
		digester.addObjectCreate( "config/event/pattern", "pl.com.itsense.analysis.event.log.PatternConf" );
		
		digester.addSetNext( "config/event", "addEvent", "pl.com.itsense.analysis.event.log.EventConf" );
		digester.addSetNext( "config/file", "addFile", "pl.com.itsense.analysis.log.event.FileConf" );
		digester.addSetNext( "config/event/pattern", "addPattern", "pl.com.itsense.analysis.event.log.Pattern" );
		
		digester.addCallMethod( "config/event/pattern", "setValue", 0);
		
		digester.addSetProperties( "config" );
		digester.addSetProperties( "config/event" );
		digester.addSetProperties( "config/file" );
		
		try 
		{
			configuration = digester.parse(file);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		}
		return configuration;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<EventConf> getEvents() 
	{
		return events;
	}
	
	/**
	 * 
	 */
	public void addEvent(final EventConf event)
	{
		events.add(event);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<FileConf> getFiles() 
	{
		return files;
	}
	
	/**
	 * 
	 * @param file
	 */
	public void addFile(final FileConf file)
	{
		files.add(file);
	}
	
	/**
	 * 
	 * @param decimalFormat
	 */
	public void setDecimalFormat(final String decimalFormat) 
	{
            this.decimalFormat = decimalFormat;
        }
	
	/**
	 * 
	 * @return
	 */
	public String getDecimalFormat() 
	{
            return decimalFormat;
        }
	
	
	@Override
	public String toString() 
	{
	    final StringBuffer sb = new StringBuffer();
	    sb.append("Configuration: decimalFormat = " + decimalFormat).append("\n");
	    for (final EventConf event : events)
	    {
	        sb.append(event).append("\n");    
	    }
            for (final FileConf file : files)
            {
                sb.append(file).append("\n");    
            }
	    return sb.toString();
	}
	
	
}
