package pl.com.itsense.analysis.event.log.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author P.Pretki
 *
 */
public class SequenceConf
{   
    /** */
    private String id;
    /** */
    private String name;
    /** */
    private List<MemberConf> terms = new ArrayList<MemberConf>();
    /** */
    private List<MeasureConf> measures = new ArrayList<MeasureConf>();
    /**
     * 
     * @param term
     */
    public void add(final MemberConf term)
    {
        if ((term != null) && !terms.contains(term))
        {
            terms.add(term);
        }
    }

    public void add(final MeasureConf measureConf)
    {
        if ((measureConf != null) && !measures.contains(measureConf))
        {
            measures.add(measureConf);
        }
    }
    
    /**
     * 
     * @return
     */
    public List<MemberConf> getTerms()
    {
        return terms;
    }
    /**
     * 
     * @param terms
     */
    public void setTerms(final List<MemberConf> terms)
    {
        this.terms = terms;
    }

    /**
     * 
     * @param measures
     */
    public void setMeasures(final List<MeasureConf> measures)
    {
        this.measures = measures;
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
    public List<MeasureConf> getMeasures()
    {
        return measures;
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
    public String getName()
    {
        return name;
    }
    /**
     * 
     * @param name
     */
    public void setName(final String name)
    {
        this.name = name;
    }
    /**
     * 
     */
    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("Sequence: id = " + id).append("\n");
        sb.append("---- Terms: ").append("\n");
        for (final MemberConf term : terms)
        {
            sb.append(term).append("\n");
        }
        sb.append("---- Measures: ").append("\n");
        for (final MeasureConf measure : measures)
        {
            sb.append(measure).append("\n");
        }
        
        return  sb.toString();
    }
}
