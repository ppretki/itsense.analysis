package pl.com.itsense.analysis.event.log.configuration;
/**
 * 
 * @author ppretki
 *
 */
public class EventConsumerConf extends PropertyHolderConf
{
    /** */
    private String type;
    /** */
    private String events;
    /***
     * 
     * @return
     */
    public String getType()
    {
        return type;
    }
    /**
     * 
     * @param type
     */
    public void setType(final String type)
    {
        this.type = type;
    }
    /**
     * 
     * @return
     */
    public String getEvents()
    {
        return events;
    }
    
    /**
     * 
     * @param events
     */
    public void setEvents(String events)
    {
        this.events = events;
    }
    
    @Override
    public String toString()
    {
        return "EventConsumer: type = " + type + ", events = " + events + ", properties : \n" + super.toString();
    }
}
