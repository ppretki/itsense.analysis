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
    /**
     * 
     * @param terms
     */
    public Sequence(final Term[] terms)
    {
        this.terms = terms;
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
        boolean finished = false;
        for (final String varName : terms[index].getVariables().keySet())
        {
            context.put(varName, event.getProperty(terms[index].getVariables().get(varName)));
        }
        if (terms[index].accept(event,resolver))
        {
            index++;
            finished = true;
        }
        return finished;
    }
    /***
     * 
     * @return
     */
    public String acceptedEventId()
    {
       return index < terms.length  ? terms[index].getEventId() : null;
    }
    
}
