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

import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.Measure;
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
	@OneToMany(targetEntity=EventDB.class, cascade={CascadeType.ALL})
	private List<EventDB> events = new ArrayList<EventDB>(); 
    /** */
    @OneToMany(targetEntity=MeasureDB.class, cascade={CascadeType.ALL})
    private List<MeasureDB> measures = new ArrayList<MeasureDB>(); 
	
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
        for (final Event event : sequence.getEvents())
        {
            events.add(new EventDB(event));
        }
        for (final String measureName : sequence.getMeasureNames())
        {
            measures.add(new MeasureDB(measureName, sequence.getMeasure(measureName)));
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
    public List<MeasureDB> getMeasures()
    {
        return measures;
    }
    /**
     * 
     * @param measures
     */
    public void setMeasures(final List<MeasureDB> measures)
    {
        this.measures = measures;
    }
}
