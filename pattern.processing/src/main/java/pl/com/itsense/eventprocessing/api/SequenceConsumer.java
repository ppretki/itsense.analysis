package pl.com.itsense.eventprocessing.api;

import pl.com.itsense.eventprocessing.configuration.SequenceConsumerConf;

/**
 * 
 * @author ppretki
 *
 */
public interface SequenceConsumer extends PropertyHolder
{
    String ALL_EVENTS_CONSUMER = "*";
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
