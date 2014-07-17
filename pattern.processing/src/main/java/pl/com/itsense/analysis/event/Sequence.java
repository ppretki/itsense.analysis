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
        if (terms[index].accept(event,resolver))
        {
            index++;
        }
        return finished;
    }
    /***
     * 
     * @return
     */
    public boolean canAcceptMoreEvents()
    {
       return index < terms.length;
    }
}
