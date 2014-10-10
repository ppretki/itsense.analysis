package pl.com.itsense.eventprocessing;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.impl.EventProcessingEngineImpl;

/**
 *
 */
public class XMLConfiguration
{
    /**
     * 
     * @param xmlConfigFile
     */
    public static EventProcessingEngine parse(final File file){
        final Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("engine", "pl.com.itsense.eventprocessing.EventProcessingEngineImpl");
        digester.addObjectCreate("engine/providers/rexpressionprovider", "pl.com.itsense.eventprocessing.provider.rexpression.RExpressionProvider");
        digester.addObjectCreate("engine/providers/rexpressionprovider/rexpression", "pl.com.itsense.eventprocessing.provider.rexpression.RExpression");
        digester.addObjectCreate("engine/providers/rexpressionprovider/rexpression/group", "pl.com.itsense.eventprocessing.provider.rexpression.RExpressionGroup");

        digester.addSetProperties("engine");
        digester.addSetProperties("engine/providers/textfileprovider");
        digester.addSetProperties("engine/providers/textfileprovider/rexpression");
        digester.addSetProperties("config/consumer");
        digester.addSetProperties("engine/consumers/sqlconsumer");

        digester.addCallMethod("config/event/pattern", "setValue", 0);

        EventProcessingEngineImpl engine = null;
        try
        {
            engine = digester.parse(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        return engine;
    }

    private XMLConfiguration()
    {
    }

}
