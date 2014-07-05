package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import pl.com.itsense.analysis.event.EventProcessingEngine;
import pl.com.itsense.analysis.event.EventProvider;
import pl.com.itsense.analysis.event.Report;
import pl.com.itsense.analysis.event.log.providers.TextFileEventProvider;

/**
 * 
 * @author ppretki
 *
 */
public class LogAnalysis 
{
	/** */
	private static String TEMP_DIRECTORY = System.getProperty("user.home"); 
	/** */
	public static void main(final String[] args) 
	{
		final Configuration configuration = Configuration.parse(new File(args[0]));
		System.out.println(configuration);
		final EventProcessingEngine engine = new EventProcessingEngine();
		
		// EVENTS PROVIDERS
		final EventProvider[] eventProviders = new EventProvider[configuration.getFiles().size()];
		for (int i = 0 ; i < eventProviders.length; i++)
		{
			final FileConf file = configuration.getFiles().get(i);
			final ArrayList<EventConf> events = new ArrayList<EventConf>();
			for (final EventConf event : configuration.getEvents())
			{
				if (file.getId().equals(event.getFileid()))
				{
					events.add(event);
				}
			}
			eventProviders[i] = new TextFileEventProvider(new File(file.getPath()), events.toArray(new EventConf[0]), file.getFrom());
		}

		// ACTION AND EVENT HANDLERS
		for (final ProcessorConf processorConf : configuration.getProcessors())
		{
			try 
			{
				final Class<?> type = Class.forName(handler.getType());
				if (ActionProcessingHandler.class.isAssignableFrom(type))
				{
					final ActionProcessingHandler handlerInstance = (ActionProcessingHandler)type.newInstance();
					for (final PropertyConf property : handler.getProperties())
					{
						handlerInstance.setProperty(property.getName(), property.getValue());
					}
					engine.addActionProcessingHandler(handlerInstance);
				}
				else if (EventProcessingHandler.class.isAssignableFrom(type))
				{
					final EventProcessingHandler handlerInstance = (EventProcessingHandler)type.newInstance();
					for (final PropertyConf property : handler.getProperties())
					{
						handlerInstance.setProperty(property.getName(), property.getValue());
					}
					engine.addEventProcessingHandler(handlerInstance);
				}
			} 
			catch (ClassNotFoundException | InstantiationException| IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}

		// REPORT
		for (final ReportConf reportConf : configuration.getReports())
        {
			try 
            {
				final Class<?> type = Class.forName(reportConf.getType());
				if (Report.class.isAssignableFrom(type))
                {
					final Report reportInstance = (Report)type.newInstance();
                    for (final PropertyConf property : reportConf.getProperties())
                    {
                    	reportInstance.setProperty(property.getName(), property.getValue());
                    }
                    engine.addReport(reportInstance);
                }
            } 
            catch (ClassNotFoundException | InstantiationException| IllegalAccessException e) 
            {
            	e.printStackTrace();
            }
        }
		
		for (final ActionConf action : configuration.getActions())
		{
			String[] openEvents = null;
			String[] closeEvents = null;
			OpenActionHandler openActionHandler = null;
			CloseActionHandler closeActionHandler = null;
			
			final String open = action.getOpen();
			final String close = action.getClose();
			final OpenActionHandlerConf openActionHandlerConf = action.getOpenActionHandlerConf();
			final CloseActionHandlerConf closeActionHandlerConf = action.getCloseActionHandlerConf();
			if ((open != null) && !open.isEmpty())
			{
				openEvents = open.split(",");
				for (int i = 0 ; i < openEvents.length; i++) openEvents[i] = openEvents[i].trim();
			}
			if ((close != null) && !close.isEmpty())
			{
				closeEvents = close.split(",");
				for (int i = 0 ; i < closeEvents.length; i++) closeEvents[i] = closeEvents[i].trim();
			}

			
			try 
			{
				if (openActionHandlerConf != null) 
				{
					if (openActionHandlerConf.getType() != null)
					{
						final Class<?> type = Class.forName(openActionHandlerConf.getType());
						if (OpenActionHandler.class.isAssignableFrom(type))
						{
							openActionHandler = (OpenActionHandler)type.newInstance();
						}
					}
					else if (openActionHandlerConf.getValue() != null)
					{
						openActionHandler = generateOpenActionHandlerClass(TEMP_DIRECTORY, openActionHandlerConf.getValue());
					}
				}
			} 
			catch (ClassNotFoundException | InstantiationException| IllegalAccessException e) 
			{
				e.printStackTrace();
			}
			
			
			try 
			{
				if (closeActionHandlerConf != null) 
				{
					if (closeActionHandlerConf.getType() != null)
					{
						final Class<?> type = Class.forName(closeActionHandlerConf.getType());
						if (CloseActionHandler.class.isAssignableFrom(type))
						{
							closeActionHandler = (CloseActionHandler)type.newInstance();
						}
					} 
					else if (closeActionHandlerConf.getValue() != null)
					{
						closeActionHandler = generateCloseActionHandlerClass(TEMP_DIRECTORY, closeActionHandlerConf.getValue());
					}
					
				}
			} 
			catch (ClassNotFoundException | InstantiationException| IllegalAccessException e) 
			{
				e.printStackTrace();
			}
			engine.addAction(action.getId(), openEvents, closeEvents, openActionHandler, closeActionHandler);	
		}
		engine.process(eventProviders);
	}

	/**
	 * 
	 */
	private static OpenActionHandler generateOpenActionHandlerClass(final String tempDirectory , final String method)
	{
		final String className = "OpenActionHandler" + Integer.toHexString(method.hashCode());
		OpenActionHandler handler = null;
		final StringBuffer sb = new StringBuffer();
		sb.append("package pl.com.itsense.analysis.event.log.handler.action.processing.generator;").append("\n");
		sb.append("import pl.com.itsense.analysis.event.Action;").append("\n");
		sb.append("import pl.com.itsense.analysis.event.Action.Status;").append("\n");
		sb.append("import pl.com.itsense.analysis.event.CloseActionHandler;").append("\n");
		sb.append("import pl.com.itsense.analysis.event.EEngine;").append("\n");
		sb.append("import pl.com.itsense.analysis.event.Event;").append("\n");
		sb.append("import pl.com.itsense.analysis.event.OpenActionHandler;").append("\n");
		sb.append("import pl.com.itsense.analysis.event.PropertyHolderImpl;").append("\n");
		sb.append("public class " + className + " extends PropertyHolderImpl implements OpenActionHandler").append("\n"); 
		sb.append("{").append("\n");
		sb.append("@Override").append("\n");
		sb.append("public Status processCloseActionEvent(final Action action, final Event event, final EEngine engine)").append("\n"); 
		sb.append("{").append("\n");
		sb.append(method).append("\n");
		sb.append("}").append("\n");
		
        try 
        {
            final File output = new File(className + ".java");
            if (output.exists())
            {
                    Files.delete(output.toPath());
            }
            Files.write((new File(className + ".java")).toPath(), sb.toString().getBytes(), StandardOpenOption.CREATE);
        } 
        catch (IOException e) 
        {
                e.printStackTrace();
        }

		
		return handler;
	}
	/**
	 * 
	 */
	private static CloseActionHandler generateCloseActionHandlerClass(final String tempDirectory , final String method)
	{
		CloseActionHandler handler = null;
		return handler;
	}
	
}
