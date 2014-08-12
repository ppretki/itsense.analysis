package pl.com.itsense.analysis.event.log.configuration;

/**
 * 
 * @author P.Pretki
 *
 */
public class MeasureConf
{
    /** */
    private String value;
    /** */
    private String name;
    /**
     * 
     * @return
     */
    public String getName()
    {
        return name;
    }
    /**
     * 
     * @return
     */
    public String getValue()
    {
        return value;
    }
    
    public void setName(final String name)
    {
        this.name = name;
    }
    /**
     * 
     * @param value
     */
    public void setValue(final String value)
    {
        this.value = value;
    }
    /**
     * 
     */
    @Override
    public String toString()
    {
        return  "Variable: name = " + name + ", value = " + value;
    }

}
