package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import pl.com.itsense.analysis.event.EventProcessingEngine;
import pl.com.itsense.analysis.event.EventProvider;

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
		final EventProcessingEngine engine = new EventProcessingEngine();
		final EventProvider[] eventProviders = new EventProvider[configuration.getFiles().size()];
		for (int i = 0 ; i < eventProviders.length; i++)
		{
			final FileConf file = configuration.getFiles().get(i);
			if (groups != null)
			{
				final ArrayList<EventConf> events = new ArrayList<EventConf>();
				for (final EventConf event : configuration.getEvents())
				{
					if (file.getId() == event.getFileid())
					{
						events.add(event);
					}
				}
				eventProviders[i] = new TextFileEventProvider(new File(file.getPath()), events.toArray(new EventConf[0]), Pattern.compile(file.getTimestamp()) , groups);	
			}
			else
			{
				System.exit(-1);
			}
		}
		engine.process(eventProviders);
		
	}

}
