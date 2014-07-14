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
@Table(name="PatternDB")
public class PatternDB 
{
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private long id;
	/** */
	private String patternId;
    /** */
    @OneToMany(targetEntity=GroupDB.class, cascade={CascadeType.ALL})
    private List<GroupDB> patterns = new ArrayList<GroupDB>(); 	
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
    public String getPatternId()
    {
        return patternId;
    }
    /**
     * 
     * @param patternId
     */
    public void setPatternId(final String patternId)
    {
        this.patternId = patternId;
    }
    /**
     * 
     * @return
     */
    public List<GroupDB> getPatterns()
    {
        return patterns;
    }
    /**
     * 
     * @param patterns
     */
    public void setPatterns(final List<GroupDB> patterns)
    {
        this.patterns = patterns;
    }
}
