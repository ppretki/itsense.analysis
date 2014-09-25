package pl.com.itsense.eventprocessing.api;

import pl.com.itsense.eventprocessing.api.Event;


/**
 *
 */
public interface Sequence
{
    /**
     * 
     * @return
     */
    Member[] getTerms();
    /**
     * 
     * @return
     */
    boolean accept(final Event event);
    /***
     * 
     * @return
     */
    String acceptedEventId();
    /**
     * 
     * @return
     */
    Event[] getEvents();
    /**
     * 
     * @return
     */
    String getId();
    /**
     * 
     * @return
     */
    String getName();
    /**
     * 
     */
    String getResolvedName();
    /**
     * 
     * @return
     */
    String[] getMeasureNames();
    /**
     * 
     * @return
     */
    double getMeasure(String name);
}
