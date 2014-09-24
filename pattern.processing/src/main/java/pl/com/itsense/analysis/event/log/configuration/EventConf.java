package pl.com.itsense.analysis.event.log.configuration;

import java.util.ArrayList;

/**
 * 
 * @author ppretki
 *
 */
public class EventConf 
{
	/** */
	private String id;
	/** */
	private final ArrayList<PatternConf> patterns = new ArrayList<PatternConf>();
	
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
	 * @return
	 */
	public void setId(final String id) 
	{
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<PatternConf> getPatterns() 
	{
		return patterns;
	}
	
	/**
	 * 
	 * @param pattern
	 */
	public void add(final PatternConf pattern)
	{
		patterns.add(pattern);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() 
	{
		final StringBuffer sb = new StringBuffer();
		sb.append("Event: id = " + id);
		for (final PatternConf pattern : patterns)
		{
			sb.append(pattern);
		}
		return sb.toString();
	}
	
	
}
