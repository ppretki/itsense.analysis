package pl.com.itsense.eventprocessing.provider.rexpression;

import java.util.HashMap;

import pl.com.itsense.eventprocessing.api.Event;

/**
 * 
 */
public class RExpressionEvent implements Event
{
    /** */
    private final RExpression rExpression;
    /** */
    private final HashMap<RExpressionGroup, String> gValues = new HashMap<RExpressionGroup, String>(); 
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
    /**
     * 
     * @return
     */
    public RExpression getRExpression()
    {
        return rExpression;
    }
    
    /**
     * 
     * @param group
     * @param value
     */
    void setGroup(final RExpressionGroup group , final String value)
    {
        gValues.put(group, value);
    }
    
    /**
     * 
     * @return
     */
    public String getGroup(final RExpressionGroup group)
    {
        return gValues.get(group);
    }
    
}
