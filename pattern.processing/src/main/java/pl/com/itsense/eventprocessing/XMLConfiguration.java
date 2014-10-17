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
        digester.addObjectCreate("engine", "pl.com.itsense.eventprocessing.impl.EventProcessingEngineImpl");
        digester.addObjectCreate("engine/providers/rexpressionprovider", "pl.com.itsense.eventprocessing.provider.rexpression.RExpressionProvider");
        digester.addObjectCreate("engine/providers/rexpressionprovider/rexpression", "pl.com.itsense.eventprocessing.provider.rexpression.RExpression");
        digester.addObjectCreate("engine/providers/rexpressionprovider/rexpression/rexpressiongroup", "pl.com.itsense.eventprocessing.provider.rexpression.RExpressionGroup");
        digester.addObjectCreate("engine/consumers/sqlconsumer", "pl.com.itsense.eventprocessing.consumer.sql.SQLConsumer");

        digester.addSetNext( "engine/providers/rexpressionprovider", "add", "pl.com.itsense.eventprocessing.provider.rexpression.RExpressionProvider" );
        digester.addSetNext( "engine/consumers/sqlconsumer", "add", "pl.com.itsense.eventprocessing.consumer.sql.SQLConsumer" );
        digester.addSetNext( "engine/providers/rexpressionprovider/rexpression", "add", "pl.com.itsense.eventprocessing.provider.rexpression.RExpression" );
        digester.addSetNext( "engine/providers/rexpressionprovider/rexpression/rexpressiongroup", "add", "pl.com.itsense.eventprocessing.provider.rexpression.RExpressionGroup" );

        digester.addSetProperties("engine");
        digester.addSetProperties("engine/providers/rexpressionprovider");
        digester.addSetProperties("engine/consumers/sqlconsumer");
        digester.addSetProperties("engine/providers/rexpressionprovider/rexpression");
        digester.addSetProperties("engine/providers/rexpressionprovider/rexpression/rexpressiongroup");

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
