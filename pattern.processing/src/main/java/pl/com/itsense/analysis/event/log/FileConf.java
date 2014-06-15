package pl.com.itsense.analysis.event.log;

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
	private String timestampProducer;
	
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
	 * @param timestampproducer
	 */
	public void setTimestampProducer(final String timestampProducer) 
	{
		this.timestampProducer = timestampProducer;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTimestampProducer() 
	{
		return timestampProducer;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() 
	{
		return "File: id = " + id + ", path = " + path + ", timestampProducer = " + timestampProducer;
	}
	
}
