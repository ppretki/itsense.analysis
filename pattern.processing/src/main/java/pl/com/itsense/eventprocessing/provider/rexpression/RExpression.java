package pl.com.itsense.eventprocessing.provider.rexpression;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.com.itsense.eventprocessing.api.Event;

/**
 * 
 * @author ppretki
 *
 */
public class RExpression
{
    private Pattern pattern;
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
        this.pattern = Pattern.compile(value);
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
    /**
     * 
     * @param line
     * @param timestamp
     * @return
     */
    public RExpressionEvent getEvent(final long timestamp, final String line)
    {
        RExpressionEvent event = null;
        if (pattern != null && line != null && timestamp > 0)
        {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find())
            {
                if (matcher.groupCount() == groups.size() + 1)
                {
                    event = new RExpressionEvent(this);
                }
            }
        }
        return event;
    }

}
