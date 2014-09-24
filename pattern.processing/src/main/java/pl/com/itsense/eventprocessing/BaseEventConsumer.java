package pl.com.itsense.eventprocessing;

import pl.com.itsense.eventprocessing.api.EventConsumer;

/**
 *
 */
public abstract class BaseEventConsumer extends PropertyHolderImpl implements EventConsumer 
{

	@Override
	public boolean isConsumed(final String eventId) 
	{
		return false;
	}

}
