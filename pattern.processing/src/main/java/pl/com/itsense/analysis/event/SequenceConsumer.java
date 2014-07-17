package pl.com.itsense.analysis.event;

import pl.com.itsense.analysis.event.log.configuration.SequenceConsumerConf;

/**
 * 
 * @author ppretki
 *
 */
public interface SequenceConsumer extends PropertyHolder
{
    /***
     * 
     * @return
     */
    String[] getSequenceIds();
    /**
     * 
     * @param sequence
     */
    void consume(Sequence sequence);
    
    /**
     * 
     */
    void configure(SequenceConsumerConf configuration);
}
