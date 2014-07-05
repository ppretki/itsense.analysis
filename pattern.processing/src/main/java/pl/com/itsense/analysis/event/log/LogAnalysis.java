package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.util.ArrayList;

import pl.com.itsense.analysis.event.EventProcessingEngine;
import pl.com.itsense.analysis.event.EventProcessor;
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
		for (final ProcessorConf processor : configuration.getProcessors())
		{
			try 
			{
				final Class<?> type = Class.forName(processor.getType());
				if (EventProcessor.class.isAssignableFrom(type))
				{
					final EventProcessor processorInstane = (EventProcessor)type.newInstance();
					for (final PropertyConf property : processor.getProperties())
					{
						processorInstane.setProperty(property.getName(), property.getValue());
					}
					engine.addEventProcessor(processorInstane);
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
		engine.process(eventProviders);
	}
	
}
