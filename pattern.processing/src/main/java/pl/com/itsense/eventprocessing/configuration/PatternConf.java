package pl.com.itsense.eventprocessing.configuration;

/**
 * 
 * @author ppretki
 * 
 */
public class PatternConf
{
    /** */
    private String id;
    /** */
    private String regExp;
    /** */
    private String value;

    /**
     * 
     * @param id
     */
    public void setId(final String id)
    {
        this.id = id;
    }

    public void setRegExp(final String regExp)
    {
        this.regExp = regExp;
    }

    /**
     * 
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * 
     * @return
     */
    public String getRegExp()
    {
        return regExp;
    }

    /**
     * 
     * @return
     */
    public String getValue()
    {
        return value;
    }

    /**
     * 
     * @param exp
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
        return "Pattern: id = " + id + ", regExp = " + regExp + ", value = " + value;
    }
}
