package pl.com.itsense.eventprocessing.provider.rexpression;

import java.util.LinkedList;

/**
 * 
 * @author ppretki
 *
 */
public class RExpression
{
    /** */
    private String value;
    /** */
    private final LinkedList<RExpressionGroup> groups = new LinkedList<RExpressionGroup>(); 
    /**
     * 
     * @param value
     */
    public void setValue(final String value)
    {
        this.value = value;
    }
    /*
     * 
     */
    public String getValue()
    {
        return value;
    }
    
    public void add(final RExpressionGroup group)
    {
        if (!groups.contains(group))
        {
            groups.add(group);
        }
    }

}
