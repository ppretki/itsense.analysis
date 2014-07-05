package pl.com.itsense.analysis.event.log.processor;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.EventProcessor;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.StatefulEventProcessor;

/**
 * 
 * @author ppretki
 *
 */
public class ConnectionMonitor extends PropertyHolderImpl implements StatefulEventProcessor
{

	@Override
	public String process(Event event, String state, EEngine engine) 
	{
		return null;
	}
}
