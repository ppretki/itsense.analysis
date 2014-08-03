package pl.com.itsense.analysis.event.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.Sequence;

@Proxy(lazy=false)
@Entity
@Table(name="SequenceDB")
public class SequenceDB 
{
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private long id;
	/** */
	private String sequenceId;
	/** */
	private String name;
	/** */
	private long duration;
	/** */
	@OneToMany(targetEntity=EventDB.class, cascade={CascadeType.ALL})
	private List<EventDB> events = new ArrayList<EventDB>(); 
	
	/**
	 * 
	 */
	public SequenceDB()
    {
    }
    /**
     * 
     */
    public SequenceDB(final Sequence sequence)
    {
        sequenceId = sequence.getId();
        name = sequence.getResolvedName();
        duration = sequence.getDuration();
        for (final Event event : sequence.getEvents())
        {
            events.add(new EventDB(event));
        }
    }
    /**
     * 
     * @return
     */
    public List<EventDB> getEvents()
    {
        return events;
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
    public String getSequenceId()
    {
        return sequenceId;
    }
    /**
     * 
     * @param events
     */
    public void setEvents(final List<EventDB> events)
    {
        this.events = events;
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
     * @param name
     */
    public void setName(final String name)
    {
        this.name = name;
    }
    /**
     * 
     * @param sequenceId
     */
    public void setSequenceId(final String sequenceId)
    {
        this.sequenceId = sequenceId;
    }
    
    /**
     * 
     * @return
     */
    public long getDuration()
    {
        return duration;
    }
    /**
     * 
     * @param duration
     */
    public void setDuration(final long duration)
    {
        this.duration = duration;
    }
}
