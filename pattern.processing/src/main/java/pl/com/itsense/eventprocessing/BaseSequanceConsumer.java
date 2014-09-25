package pl.com.itsense.eventprocessing;

import java.util.ArrayList;

import pl.com.itsense.eventprocessing.PropertyHolderImpl;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycle;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycleListener;
import pl.com.itsense.eventprocessing.api.SequenceConsumer;
import pl.com.itsense.eventprocessing.configuration.SequenceConsumerConf;

/**
 *
 */
public abstract class BaseSequanceConsumer extends PropertyHolderImpl implements SequenceConsumer,  ProcessingLifecycleListener
{
    /** */
    private String[] acceptedSeqIds;
    /**
     * 
     */
    @Override
    public final String[] getSequenceIds()
    {
        return acceptedSeqIds;
    }
    /**
     * 
     */
    @Override
    public final void configure(final SequenceConsumerConf configuration)
    {
        final ArrayList<String> list = new ArrayList<String>();
        final String sequences = configuration.getSequences();
        if (sequences != null && sequences.length() > 0)
        {
            for (final String sequenceId : sequences.split(","))
            {
                list.add(sequenceId.trim());
            }
        }
        acceptedSeqIds = list.toArray(new String[0]);

    }
    /**
     * 
     */
    @Override
    public void enter(final ProcessingLifecycle lifecycle, final EventProcessingEngine engine)
    {
        switch (lifecycle)
        {
            case START:
                processingStart(engine);
                break;
            case FINISH:
                processingFinish(engine);
                break;
            
        }
    }
    /** */
    protected void processingStart(final EventProcessingEngine engine)
    {
        
    }
    
    /** */
    protected void processingFinish(final EventProcessingEngine engine)
    {
        
    }

}
