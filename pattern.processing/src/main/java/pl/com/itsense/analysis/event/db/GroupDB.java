package pl.com.itsense.analysis.event.db;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

@Proxy(lazy=false)
@Entity
@Table(name="GroupDB")
public class GroupDB 
{
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private long id;
	/** */
	private int groupId;
	/** */
	private String value;
	
	/**
	 * 
	 * @param groupId
	 */
	public void setGroupId(final int groupId)
    {
        this.groupId = groupId;
    }
	/**
	 * 
	 * @return
	 */
	public int getGroupId()
    {
        return groupId;
    }
	/**
	 * 
	 * @param id
	 */
	public void setId(final long id)
    {
        this.id = id;
    }
	/**
	 * 
	 * @return
	 */
	public long getId()
    {
        return id;
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
	 * @return
	 */
	public String getValue()
    {
        return value;
    }
}
