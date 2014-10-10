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
    /** */
    private Class eventClass;
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
    public Event getEvent(final String line, final long timestamp)
    {
        Event result = null;
        if (pattern != null && line != null)
        {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find())
            {
                if (matcher.groupCount() == groups.size() + 1)
                {
                    final Object instance = eventClass.newInstance();
                    eventClass.getMethod("get", parameterTypes);
                    Method lMethod = c.getMethod("showLong", cArg);
                    
                }
            }
        }
        
        
        
        if (matcher.find())
        {
            
            final long timestamp = parseDateTimeStamp(line);
            if (timestamp > -1)
            {
                final TextLine textLineEvent = new TextLine(event.getId(), timestamp);
                textLineEvent.setProperty(Event.PROPERTY_LINE, line);
                if (matcher.groupCount() > 0)
                {
                    final PatternConf patternConf = patternDefs.get(pattern);
                    for (int i = 1 ; i < (matcher.groupCount()+1); i++)
                    {
                        textLineEvent.setProperty(patternConf.getId() + "$" + i, matcher.group(i));
                    }
                }
                return textLineEvent;   
        return result;
    }

}
