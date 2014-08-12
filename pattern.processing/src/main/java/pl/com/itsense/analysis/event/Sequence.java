package pl.com.itsense.analysis.event;

import java.util.HashMap;
import java.util.List;

import org.mvel2.MVEL;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import pl.com.itsense.analysis.event.log.configuration.MeasureConf;


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
    private final Term[] terms;
    /** */
    private final HashMap<String, MeasureImpl> measures = new HashMap<String, MeasureImpl>();
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
    public Sequence(final Term[] terms,final List<MeasureConf> measureConfs, final String name, final String id)
    {
        this.terms = terms;
        for (final MeasureConf measureConf : measureConfs)
        {
             measures.put(measureConf.getName(), new MeasureImpl(measureConf));
        }
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
        sb.append("Sequence: id = " + id + ", name = " + name + ", symbolicName = " + getResolvedName() + "\n");
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
    public String[] getMeasureNames()
    {
        return measures.keySet().toArray(new String[0]);
    }
    /**
     * 
     * @return
     */
    public double getMeasure(final String name)
    {
        double result = 0;
        final MeasureImpl measure = measures.get(name);
        if (measure != null)
        {
            result = measure.getValue();
        }
        return result;
    }
    
    
    /**
     * 
     * @author P.Pretki
     *
     */
    private class MeasureImpl implements Measure
    {
        /** */
        private final String name;
        /** */
        private final String expression;
        /** */
        private Double value;  
        /**
         * 
         * @param m
         */
        public MeasureImpl(final MeasureConf measureConf)
        {
            this.name = measureConf.getName();
            this.expression  = measureConf.getValue();
            this.value = null;
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
         * @return
         */
        public Double getValue()
        {
            if (expression != null)
            {
                value = (Double)MVEL.eval(expression, resolver);
            }
            return value;
        }
    }

    
}
