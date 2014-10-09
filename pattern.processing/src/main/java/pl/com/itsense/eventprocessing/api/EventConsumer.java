package pl.com.itsense.eventprocessing.api;

/**
 *
 */
public interface EventConsumer
{
    /** */
    void process(Event event);
}
