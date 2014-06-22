package pl.com.itsense.analysis.event;

import pl.com.itsense.analysis.event.Action.Status;

/**
 * 
 * @author ppretki
 *
 */
public interface CloseActionHandler extends PropertyHolder
{
	Status processCloseActionEvent(Action action, Event event, EEngine engine); 
}
