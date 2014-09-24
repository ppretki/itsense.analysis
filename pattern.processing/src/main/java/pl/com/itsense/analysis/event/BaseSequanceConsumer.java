package pl.com.itsense.analysis.event;

import java.util.ArrayList;

import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.log.configuration.SequenceConsumerConf;
import pl.com.itsense.eventprocessing.api.EEngine;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycle;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycleListener;
import pl.com.itsense.eventprocessing.api.SequenceConsumer;

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
    public void enter(final ProcessingLifecycle lifecycle, final EEngine engine)
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
    protected void processingStart(final EEngine engine)
    {
        
    }
    
    /** */
    protected void processingFinish(final EEngine engine)
    {
        
    }

}
