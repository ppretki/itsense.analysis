package pl.com.itsense.eventprocessing.provider.rexpression;
/**
 * 
 * @author P.Pretki
 *
 */
public class RExpressionGroup implements Comparable<RExpressionGroup>
{
    /** */
    private int number;
    /** */
    private String type;
    /** */
    private String name;
    
    @Override
    public int compareTo(final RExpressionGroup o)
    {
        return number - o.number;
    }
    /**
     * 
     * @param name
     */
    public void setName(final String name)
    {
        this.name = name;
    }
    /**
     * 
     * @param number
     */
    public void setNumber(final int number)
    {
        this.number = number;
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
    public String getName()
    {
        return name;
    }
    /**
     * 
     * @return
     */
    public int getNumber()
    {
        return number;
    }
    /**
     * 
     * @return
     */
    public String getType()
    {
        return type;
    }
}
