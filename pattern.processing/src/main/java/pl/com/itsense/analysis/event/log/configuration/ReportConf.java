package pl.com.itsense.analysis.event.log.configuration;

/**
 * 
 * @author ppretki
 *
 */
public class ReportConf extends PropertyHolderConf
{
    /** */
    private String type;
    
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

    @Override
    public String toString()
    {
        return  "ReportConf: type = " + type + "\n" + super.toString();
    }
}
