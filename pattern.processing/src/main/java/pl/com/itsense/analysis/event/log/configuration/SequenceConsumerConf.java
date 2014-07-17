package pl.com.itsense.analysis.event.log.configuration;

/**
 * 
 * @author ppretki
 *
 */
public class SequenceConsumerConf extends PropertyHolderConf
{
    /** */
    private String type;
    /** */
    private String sequences;
    
    /**
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
    public String getSequences()
    {
        return sequences;
    }
    /**
     * 
     * @param sequences
     */
    public void setSequences(final String sequences)
    {
        this.sequences = sequences;
    }
}
