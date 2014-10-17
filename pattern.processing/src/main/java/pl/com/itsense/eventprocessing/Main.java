package pl.com.itsense.eventprocessing;

import java.io.File;

import pl.com.itsense.eventprocessing.api.EventProcessingEngine;

/**
 *
 */
public class Main
{
    /**
     * 
     */
    private Main()
    {
    }
    
    /** */
    public static void main(final String[] args)
    {
        final EventProcessingEngine engine = XMLConfiguration.parse(new File("/home/P.Pretki/git/itsense.analysis/pattern.processing/src/main/java/config.xml"));
        engine.run();
    }
}
