package pl.com.itsense.analysis.event.log.configuration;

/**
 * 
 * @author ppretki
 *
 */
public class FileConf 
{
	/** */
	private String id;
	/** */
	private String path;
	/** */
	private String from;
	
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
	public String getPath() 
	{
		return path;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(final String id) 
	{
		this.id = id;
	}
	/***
	 * 
	 * @param path
	 */
	public void setPath(final String path) 
	{
		this.path = path;
	}
	
	/**
	 * 
	 * @param from
	 */
	public void setFrom(final String from) 
	{
            this.from = from;
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
	 */
	@Override
	public String toString() 
	{
		return "File: id = " + id + ", path = " + path;
	}
	
}
