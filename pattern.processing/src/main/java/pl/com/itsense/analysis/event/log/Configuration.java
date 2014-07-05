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
	/** */
	private final ArrayList<HandlerConf> handlers = new ArrayList<HandlerConf>();
	/** */
	private final ArrayList<ReportConf> reports = new ArrayList<ReportConf>();
	/** */
	private final ArrayList<ProcessorConf> processors = new ArrayList<ProcessorConf>();
	
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
		digester.addObjectCreate( "config/processor", "pl.com.itsense.analysis.event.log.ProcessorConf" );
		digester.addObjectCreate( "config/event/pattern", "pl.com.itsense.analysis.event.log.PatternConf" );

		
		
		digester.addSetNext( "config/action", "addAction", "pl.com.itsense.analysis.event.log.ActionConf" );
		digester.addSetNext( "config/handler", "addHandler", "pl.com.itsense.analysis.event.log.HandlerConf" );
		digester.addSetNext( "config/event", "addEvent", "pl.com.itsense.analysis.event.log.EventConf" );
		digester.addSetNext( "config/file", "addFile", "pl.com.itsense.analysis.log.event.FileConf" );
		digester.addSetNext( "config/report", "addReport", "pl.com.itsense.analysis.log.event.ReportConf" );
		digester.addSetNext( "config/event/pattern", "addPattern", "pl.com.itsense.analysis.event.log.Pattern" );
		digester.addSetNext( "config/handler/property", "addProperty", "pl.com.itsense.analysis.event.log.PropertyConf" );
		digester.addSetNext( "config/report/property", "addProperty", "pl.com.itsense.analysis.event.log.PropertyConf" );
		digester.addSetNext( "config/action/openactionhandler", "setOpenActionHandlerConf", "pl.com.itsense.analysis.event.log.OpenActionHandlerConf");
		digester.addSetNext( "config/action/closeactionhandler", "setCloseActionHandlerConf", "pl.com.itsense.analysis.event.log.CloseActionHandlerConf");
		
		
		digester.addCallMethod( "config/action/openactionhandler", "setValue", 0);
		digester.addCallMethod( "config/action/openactionhandler", "setValue", 0);
		
		digester.addSetProperties( "config" );
		digester.addSetProperties( "config/action" );
		digester.addSetProperties( "config/handler" );
		digester.addSetProperties( "config/handler/param" );
		digester.addSetProperties( "config/event" );
		digester.addSetProperties( "config/event/pattern" );
		digester.addSetProperties( "config/file" );
		digester.addSetProperties( "config/report" );
        digester.addSetProperties( "config/handler/property" );
        digester.addSetProperties( "config/report/property" );
		digester.addSetProperties( "config/action/openactionhandler" );
		digester.addSetProperties( "config/action/closeactionhandler" );
		
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
	 * @param file
	 */
	public void addProcessor(final ProcessorConf action)
	{
		processors.add(action);
	}
	

	/**
	 * 
	 * @return
	 */
	public ArrayList<ReportConf> getReports() 
	{
		return reports;
	}
	
	/**
	 * 
	 * @param file
	 */
	public void addReport(final ReportConf report)
	{
		reports.add(report);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ProcessorConf> getProcessors() 
	{
		return processors;
	}
	
	
	@Override
	public String toString() 
	{
	    final StringBuffer sb = new StringBuffer();
	    sb.append("Configuration:").append("\n");
	    for (final EventConf event : events)
	    {
	        sb.append(event).append("\n");    
	    }
        for (final FileConf file : files)
        {
        	sb.append(file).append("\n");    
        }
        for (final HandlerConf handler : handlers)
        {
        	sb.append(handler).append("\n");
        }
        for (final ReportConf report : reports)
        {
        	sb.append(report).append("\n");
        }
        for (final ProcessorConf action : processors)
        {
        	sb.append(action).append("\n");
        }
            
	    return sb.toString();
	}
	
	
}
