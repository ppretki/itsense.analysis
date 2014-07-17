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
	private String from;
	/** */
	private String to;
	/** */
	private String fileid;
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
	public String getFrom() 
	{
		return from;
	}

	/**
	 * 
	 * @return
	 */
	public void setFrom(final String from) 
	{
		this.from = from;
	}
	/**
	 * 
	 * @return
	 */
	public String getTo() 
	{
		return to;
	}

	/**
	 * 
	 * @param to
	 */
	public void setTo(final String to) 
	{
		this.to = to;
	}

	/**
	 * 
	 * @return
	 */
	public String getFileid() 
	{
		return fileid;
	}

	/**
	 * 
	 * @param fileid
	 */
	public void setFileid(final String fileid) 
	{
		this.fileid = fileid;
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
		sb.append("Event: id = " + id + ", from = " + from + ", to = " + to + ", fileid = " + fileid);
		for (final PatternConf pattern : patterns)
		{
			sb.append(pattern);
		}
		return sb.toString();
	}
	
	
}
