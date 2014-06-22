package pl.com.itsense.analysis.event.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

@Proxy(lazy=false)
@Entity
@Table(name="Pattern")
public class PatternDB 
{
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private long patternId;
	/** */
	private String id;
	/** */
	private String group1;
	/** */
	private String group2;
	
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
	 * @param id
	 */
	public void setId(String id) 
	{
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public String getGroup1() 
	{
		return group1;
	}
	
	/**
	 * 
	 * @param group1
	 */
	public void setGroup1(final String group1) 
	{
		this.group1 = group1;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getGroup2() 
	{
		return group2;
	}
	
	/**
	 * 
	 * @param group2
	 */
	public void setGroup2(final String group2) 
	{
		this.group2 = group2;
	}

}