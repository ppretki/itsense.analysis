package pl.com.itsense.eventprocessing;

import java.util.HashMap;

import org.mvel2.MVEL;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import pl.com.itsense.analysis.event.log.configuration.MemberConf;
import pl.com.itsense.analysis.event.log.configuration.VarConf;
import pl.com.itsense.eventprocessing.api.Event;
import pl.com.itsense.eventprocessing.api.Member;

/**
 * 
 * @author P.Pretki
 *
 */
public class MemberImpl implements Member
{
    /** */
    private final String mvel;
    /** */
    private final String eventId;
    /** */
    private final HashMap<String,String> variables = new HashMap<String,String>();
    /**
     * 
     * @param term
     */
    public MemberImpl(final MemberConf term)
    {
        mvel = term.getCondition() == null ? null : term.getCondition().getMvel();
        eventId = term.getEvent() == null ? null : term.getEvent().trim();
        if (term.getVariables() != null)
        {
            for (final VarConf var : term.getVariables())
            {
                if ((var.getName() != null) && (var.getValue() != null))
                {
                    variables.put(var.getName(), var.getValue());
                }
            }
        }
        
    }
    
    /**
     * @return
     */
    public String getEventId()
    {
        return eventId;
    }
    /**
     * 
     * @return
     */
    public String getMvel()
    {
        return mvel;
    }
    /**
     * 
     * @return
     */
    public boolean accept(final Event event, final MapVariableResolverFactory resolver)
    {
        boolean accept = false;
        if ((eventId != null) && (eventId.equals(event.getId())))
        {
            accept = mvel == null ? true : (Boolean)MVEL.eval(mvel, resolver);
        }
        return accept;
    }
    /**
     * 
     * @return
     */
    public HashMap<String, String> getVariables()
    {
        return variables;
    }
}
