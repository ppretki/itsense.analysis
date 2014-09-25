package pl.com.itsense.eventprocessing.configuration;

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
	private String top;
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
	
	public String getTop()
    {
        return top;
    }
	
	public void setTop(final String top)
    {
        this.top = top;
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
