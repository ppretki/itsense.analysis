package pl.com.itsense.eventprocessing.provider.rexpression;
/**
 * 
 * @author P.Pretki
 *
 */
public class RExpressionGroup implements Comparable<RExpressionGroup>
{
    /** */
    private int index;
    /** */
    private String type;
    /** */
    private String name;
    
    @Override
    public int compareTo(final RExpressionGroup o)
    {
        return index - o.index;
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
    public void setIndex(final int index)
    {
        this.index = index;
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
    public int getIndex()
    {
        return index;
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
