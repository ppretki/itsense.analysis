package pl.com.itsense.eventprocessing.db;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    /** */
    @ManyToOne(targetEntity=SequenceDB.class, cascade={CascadeType.ALL})
    private SequenceDB sequenceDB;

	/**
	 * 
	 */
	public MeasureDB()
    {
    }

	/**
     * 
     */
    public MeasureDB(final String name, final Double value, final SequenceDB parent)
    {
        this.name = name;
        this.value = value;
        this.sequenceDB = parent;
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
	
	public SequenceDB getSequenceDB()
	{
	    return sequenceDB;
	}
	    
	public void setSequenceDB(final SequenceDB sequenceDB)
	{
	    this.sequenceDB = sequenceDB;
	}

	
}
