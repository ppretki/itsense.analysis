package pl.com.itsense.eventprocessing;

import java.io.File;

import pl.com.itsense.eventprocessing.api.Granularity;
import pl.com.itsense.eventprocessing.api.ProgressEvent;
import pl.com.itsense.eventprocessing.api.ProgressListener;

/**
 *
 */
public class LogFileAnalysis
{
    /**
     * 
     */
    private LogFileAnalysis()
    {
    }
    
    /** */
    public static void main(final String[] args)
    {
        final XMLConfiguration configuration = XMLConfiguration.parse(new File(args[0]));
        final EventProcessingEngineImpl engine = new EventProcessingEngineImpl(configuration);
        engine.add(new ProgressListener()
        {
            @Override
            public void change(final ProgressEvent event)
            {
                System.out.println(event.getProgress());
            }
        }, Granularity.PERCENT);
        engine.execute();
    }
}
