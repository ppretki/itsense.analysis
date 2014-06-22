package pl.com.itsense.analysis.event;

import pl.com.itsense.analysis.event.Action.Status;

public interface OpenActionHandler 
{
	Status processOpenActionEvent(Event event, EEngine engine); 
}
