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
        this.name = name;
        this.id = id;
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
        for (final String varName : terms[index].getVariables().keySet())
        {
            context.put(varName, event.getProperty(terms[index].getVariables().get(varName)));
        }
        if (terms[index].accept(event,resolver))
        {
            events[index] = event;
            index++;
            accepted = true;
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
            name = name.replace("$("+varName+")", context.get(varName).toString());
        }
        return resolvedName;
    }
    /**
     * 
     */
    @Override
    public String toString()
    {
        return "Sequence: id = " + id + ", name = " + name + ", resolvcedName = " + getResolvedName();
    }
    
}
