package pl.com.itsense.analysis.event.log.processor;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.StatefulEventProcessor;

/**
 * 
 * @author ppretki
 *
 */
public class StateActivationMonitor extends PropertyHolderImpl implements StatefulEventProcessor
{
	/** */
	private static String EVENT_PATH_CHANGE = "PATH_CHANGE";
	/** */
	private static String EVENT_STATE_ACTIVATE = "STATE_ACTIVATE";
	/**
	 * 
	 */
	@Override
	public String process(final Event event, final String state, final EEngine engine) 
	{
		final String nextState; 
		if (EVENT_PATH_CHANGE.equals(event.getId()) && (state == null))
		{
			final String name = event.getProperty("$pathChange[2]");
			nextState = "START:" +  name;
		}
		else if (EVENT_STATE_ACTIVATE.equals(state))
		{
			nextState = null;
		}
		else
		{
			nextState = null;
		}
			
		return nextState;
	}
}
