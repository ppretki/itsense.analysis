package pl.com.itsense.analysis.event.log.configuration;

import java.util.ArrayList;

/**
 * 
 * @author P.Pretki
 *
 */
public class TermConf
{
    /** */
    private String event;
    /** */
    private String index;
    /** */
    private ArrayList<VarConf> variables = new ArrayList<VarConf>();
    /** */
    private ConditionConf condition;
    
    /**
     * 
     * @return
     */
    public String getIndex()
    {
        return index;
    }
    /**
     * 
     * @return
     */
    public String getTerm()
    {
        return event;
    }
    /**
     * 
     * @param index
     */
    public void setIndex(final String index)
    {
        this.index = index;
    }
    /**
     * 
     * @param term
     */
    public void setTerm(final String term)
    {
        this.event = term;
    }
    /**
     * 
     * @return
     */
    public ArrayList<VarConf> getVariables()
    {
        return variables;
    }
    /**
     * 
     * @param variables
     */
    public void setVariables(final ArrayList<VarConf> variables)
    {
        this.variables = variables;
    }
    /**
     * 
     * @param event
     */
    public void setEvent(final String event)
    {
        this.event = event;
    }
    /**
     * 
     * @return
     */
    public String getEvent()
    {
        return event;
    }
    /**
     * 
     */
    public void add(final VarConf variable)
    {
        if (variable != null && !variables.contains(variable))
        {
            variables.add(variable);
        }
    }
    /**
     * 
     * @param condition
     */
    public void setCondition(final ConditionConf condition)
    {
        this.condition = condition;
    }
    /**
     * 
     * @return
     */
    public ConditionConf getCondition()
    {
        return condition;
    }
}