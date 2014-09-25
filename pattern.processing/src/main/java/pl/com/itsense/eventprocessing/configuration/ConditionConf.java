package pl.com.itsense.eventprocessing.configuration;
/**
 * 
 * @author P.Pretki
 *
 */
public class ConditionConf
{
    /** */
    private String mvel;
    /**
     * 
     * @return
     */
    public String getMvel()
    {
        return mvel;
    }
    /**
     * 
     * @param mvel
     */
    public void setMvel(final String mvel)
    {
        this.mvel = mvel;
    }
    /**
     * 
     */
    @Override
    public String toString()
    {
        return "Condition: mvel = " + mvel;
    }
}
