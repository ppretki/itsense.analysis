package pl.com.itsense.eventprocessing.provider.rexpression;

import pl.com.itsense.eventprocessing.api.Event;

/**
 * 
 */
public class RExpressionEvent implements Event
{
    /** */
    private final RExpression rExpression;
    /**
     * 
     */
    RExpressionEvent(final RExpression rExpression)
    {
        this.rExpression = rExpression;
    }
    /**
     * 
     * @return
     */
    @Override
    public long getTimestamp()
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return null;
    }
}
