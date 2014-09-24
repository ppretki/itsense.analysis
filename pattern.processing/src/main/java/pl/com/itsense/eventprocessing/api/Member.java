package pl.com.itsense.eventprocessing.api;

import java.util.HashMap;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import pl.com.itsense.eventprocessing.api.Event;

/**
 *
 */
public interface Member
{
    /**
     * @return
     */
    String getEventId();
    /**
     * 
     * @return
     */
    String getMvel();
    /**
     * 
     * @return
     */
   boolean accept(final Event event, final MapVariableResolverFactory resolver);
    /**
     * 
     * @return
     */
    HashMap<String, String> getVariables();
}
