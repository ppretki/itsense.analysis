package pl.com.itsense.analysis.event.log;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
			eventProviders[i] = new TextFileEventProvider(new File(file.getPath()), events.toArray(new EventConf[0]));
		}
		final StatisticsCollector collector = new StatisticsCollector();
		engine.addProcessingHandler(collector);
		engine.process(eventProviders);
		System.out.println(collector);
		final DecimalFormat formatter = (configuration.getDecimalFormat() != null) ? new DecimalFormat(configuration.getDecimalFormat()) : null; 
		StatisticsCollector.createReport("/home/P.Pretki/MyProjects/results/Pings.html", collector.getStatistics(), formatter);
	}

}
