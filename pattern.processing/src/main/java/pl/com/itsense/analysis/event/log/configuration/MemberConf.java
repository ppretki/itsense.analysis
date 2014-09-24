package pl.com.itsense.analysis.event.log.configuration;

import java.util.ArrayList;

/**
 * 
 * @author P.Pretki
 *
 */
public class MemberConf
{
    /** */
    private String event;
    /** */
    private ArrayList<VarConf> variables = new ArrayList<VarConf>();
    /** */
    private ConditionConf condition;
    
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
    
    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("Term: event = " + event + ", condition = " + condition + "variables: ").append("\n");
        for (final VarConf var : variables)
        {
            sb.append(var).append("\n");
        }
        return  sb.toString();
    }

}
