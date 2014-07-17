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
    private List<TermConf> terms = new ArrayList<TermConf>();
    /**
     * 
     * @param term
     */
    public void add(final TermConf term)
    {
        if ((term != null) && !terms.contains(term))
        {
            terms.add(term);
        }
    }
    /**
     * 
     * @return
     */
    public List<TermConf> getTerms()
    {
        return terms;
    }
    /**
     * 
     * @param terms
     */
    public void setTerms(final List<TermConf> terms)
    {
        this.terms = terms;
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
     */
    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("Sequence: id = " + id + ", terms:").append("\n");
        for (final TermConf term : terms)
        {
            sb.append(term).append("\n");
        }
        return  sb.toString();
    }
}
