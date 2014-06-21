package pl.com.itsense.analysis.event.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

@Proxy(lazy=false)
@Entity
@Table(name="Event")
public class EventDB 
{
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private long eventId;
	/** */
	private long timestamp;
	/** */
	private String id;
	/** */
	@OneToMany(targetEntity=PatternDB.class, cascade={CascadeType.ALL})
	private List<PatternDB> patterns = new ArrayList<PatternDB>(); 
	/**
	 * 
	 * @return
	 */
	public long getTimestamp() 
	{
		return timestamp;
	}
	
	/**
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp) 
	{
		this.timestamp = timestamp;
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
	public long getEventId() 
	{
		return eventId;
	}
	
	/**
	 * 
	 * @param eventId
	 */
	public void setEventId(final long eventId) 
	{
		this.eventId = eventId;
	}
	
	/**
	 * 
	 * @param patterns
	 */
	public void setPatterns(final List<PatternDB> patterns) 
	{
		this.patterns = patterns;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PatternDB> getPatterns() 
	{
		return patterns;
	}

}
