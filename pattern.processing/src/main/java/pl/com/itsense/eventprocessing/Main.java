package pl.com.itsense.eventprocessing;

import java.io.File;

import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.Granularity;
import pl.com.itsense.eventprocessing.api.ProgressEvent;
import pl.com.itsense.eventprocessing.api.ProgressListener;
import pl.com.itsense.eventprocessing.impl.EventProcessingEngineImpl;

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
        final EventProcessingEngine engine = XMLConfiguration.parse(new File(args[0]));
        engine.run();
    }
}
