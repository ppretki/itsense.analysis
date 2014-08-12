package pl.com.itsense.analysis.event.db;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

@Proxy(lazy=false)
@Entity
@Table(name="MeasureDB")
public class MeasureDB 
{
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private long id;
	/** */
	private String name;
	/** */
	private Double value;
	/**
	 * 
	 */
	public MeasureDB()
    {
    }

	/**
     * 
     */
    public MeasureDB(final String name, final Double value)
    {
        this.name = name;
        this.value = value;
    }
	
	/**
	 * 
	 * @return
	 */
	public long getId()
    {
        return id;
    }
	
	public void setId(final long id)
    {
        this.id = id;
    }
	/**
	 * 
	 * @return
	 */
	public String getName()
    {
        return name;
    }
	public void setName(final String name)
    {
        this.name = name;
    }
	/**
	 * 
	 * @return
	 */
	public Double getValue()
    {
        return value;
    }
	/**
	 * 
	 * @param value
	 */
	public void setValue(final Double value)
    {
        this.value = value;
    }
	
	
}
