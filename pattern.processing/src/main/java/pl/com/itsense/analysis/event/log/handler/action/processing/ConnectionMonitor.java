package pl.com.itsense.analysis.event.log.handler.action.processing;

import pl.com.itsense.analysis.event.Action;
import pl.com.itsense.analysis.event.Action.Status;
import pl.com.itsense.analysis.event.CloseActionHandler;
import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.OpenActionHandler;
import pl.com.itsense.analysis.event.PropertyHolderImpl;

/**
 * 
 * @author ppretki
 *
 */
public class ConnectionMonitor extends PropertyHolderImpl implements OpenActionHandler, CloseActionHandler 
{

	@Override
	public Status processCloseActionEvent(final Action action, final Event event, final EEngine engine) 
	{
		final Event openEvent = action.getEvent(Status.OPEN);
		if (openEvent.getProperty("$1").equals(event.getProperty("$1")))
		{
			if (event.getTimestamp() -  openEvent.getTimestamp()  < 10000)
			{
				return Status.CLOSE;	
			}
			else
			{
				return Status.TERMINATE;
			}
		}
		return null;
	}

	@Override
	public Status processOpenActionEvent(final Event event, final EEngine engine) 
	{
		if (event.getProperty("$1") != null)
		{
			return Status.OPEN;
		}
		return null;
	}

}
