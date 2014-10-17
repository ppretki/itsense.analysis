package pl.com.itsense.eventprocessing.provider.rexpression;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author ppretki
 *
 */
public class RExpression
{
    /** */
    private String id;
    /** */
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
    RExpressionEvent getEvent(final long timestamp, final String line)
    {
        RExpressionEvent event = null;
        if (pattern != null && line != null && timestamp > 0)
        {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find())
            {
                System.out.println(matcher.groupCount());
                if (matcher.groupCount() == groups.size())
                {
                    for (final RExpressionGroup group : groups)
                    {
                        try
                        {
                            final String groupValue = matcher.group(group.getIndex());
                            event = new RExpressionEvent(this);
                        }
                        catch (IndexOutOfBoundsException e)
                        {
                        }
                        catch(IllegalStateException e)
                        {
                        }
                    }
                }
            }
        }
        return event;
    }
    /**
     * 
     * @return
     */
    public String getId()
    {
        return id;
    }
    /**
     * 
     * @param id
     */
    public void setId(final String id)
    {
        this.id = id;
    }

    /**
     * 
     * @return
     */
    public RExpressionGroup[] getGroups()
    {
        return groups.toArray(new RExpressionGroup[0]);
    }
}
