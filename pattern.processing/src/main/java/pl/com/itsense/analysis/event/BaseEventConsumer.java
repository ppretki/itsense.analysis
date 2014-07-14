package pl.com.itsense.analysis.event;

/**
 * 
 * @author ppretki
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
