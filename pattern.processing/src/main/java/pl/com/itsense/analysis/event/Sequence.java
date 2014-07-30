package pl.com.itsense.analysis.event;

import java.util.HashMap;

import org.mvel2.integration.impl.MapVariableResolverFactory;


/**
 * 
 * @author P.Pretki
 *
 */
public class Sequence
{
    /** */
    private HashMap<String,Object> context = new HashMap<String,Object>();  
    /** */
    private MapVariableResolverFactory resolver = new MapVariableResolverFactory(context); 
    /** */
    private Term[] terms;
    /** */
    private int index;
    /** */
    private String name;
    /** */
    private final String id;
    /** */
    private Event[] events;
    /**
     * 
     * @param terms
     */
    public Sequence(final Term[] terms, final String name, final String id)
    {
        this.terms = terms;
        this.id = id;
        this.name = name == null ? id : name;
        this.events = new Event[terms.length];
    }
    /**
     * 
     * @return
     */
    public Term[] getTerms()
    {
        return terms;
    }
    /**
     * 
     * @return
     */
    public boolean accept(final Event event)
    {
        
        boolean accepted = false;
        if (index < terms.length)
        {
            for (final String varName : terms[index].getVariables().keySet())
            {
                final Object varValue = event.getProperty(terms[index].getVariables().get(varName));
                if (varValue != null)
                {
                    context.put(varName, varValue);
                }
            }
            if (terms[index].accept(event,resolver))
            {
                events[index] = event;
                index++;
                accepted = true;
            }
        }
        return accepted;
    }
    /***
     * 
     * @return
     */
    public String acceptedEventId()
    {
       return index < terms.length  ? terms[index].getEventId() : null;
    }
    /**
     * 
     * @return
     */
    public Event[] getEvents()
    {
        return events;
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
     * @return
     */
    public String getName()
    {
        return name;
    }
    /**
     * 
     */
    public String getResolvedName()
    {
        String resolvedName = name;
        for (final String varName : context.keySet())
        {
            resolvedName = resolvedName.replace("$("+varName+")", context.get(varName).toString());
        }
        return resolvedName;
    }
    /**
     * 
     */
    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("Sequence: id = " + id + ", name = " + name + ", symbolicName = " + getResolvedName() + ", duration = " + getDuration() + "\n");
        for (int i = 0; i < events.length; i++)
        {
            if (events[i] != null)
            {
                sb.append(events[i]).append("\n");
            }
            else
            {
                break;
            }
            
        }
        return sb.toString();
    }
    /**
     * 
     * @return
     */
    public long getDuration()
    {
        long duration = -1; 
        if (events[0] != null)
        {
            duration = events[index - 1] == null ? 0 : (events[index - 1].getTimestamp() - events[0].getTimestamp());
        }
        return duration;
    }
    
}
