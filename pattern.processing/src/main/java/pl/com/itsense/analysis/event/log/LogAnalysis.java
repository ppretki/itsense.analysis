package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.util.ArrayList;

import pl.com.itsense.analysis.event.ActionProcessingHandler;
import pl.com.itsense.analysis.event.EventProcessingEngine;
import pl.com.itsense.analysis.event.EventProcessingHandler;
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
	public static void main(final String[] args) 
	{
		final Configuration configuration = Configuration.parse(new File(args[0]));
		System.out.println(configuration);
		final EventProcessingEngine engine = new EventProcessingEngine();
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
		
		for (final HandlerConf handler : configuration.getHandlers())
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
		engine.process(eventProviders);
	}

}
