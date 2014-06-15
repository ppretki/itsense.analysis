package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

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
	/**
	 * 
	 * @param xmlConfigFile
	 */
	public static final Configuration parse(final File file)
	{
		Configuration configuration = null;
		
		final Digester digester = new Digester();
		digester.setValidating( false );
		digester.addObjectCreate( "config", "pl.com.itsense.analysis.log.Configuration" );
		digester.addObjectCreate( "config/event", "pl.com.itsense.analysis.log.EventConf" );
		digester.addObjectCreate( "config/file", "pl.com.itsense.analysis.log.FileConf" );
		digester.addObjectCreate( "config/event/pattern", "pl.com.itsense.analysis.log.PatternConf" );
		
		digester.addSetNext( "config/event", "addEvent", "pl.com.itsense.analysis.log.EventConf" );
		digester.addSetNext( "config/file", "addFile", "pl.com.itsense.analysis.log.FileConf" );
		digester.addSetNext( "config/event/pattern", "addPattern", "pl.com.itsense.analysis.log.Pattern" );
		
		digester.addCallMethod( "config/event/pattern", "setValue", 0);
		
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
	
}
