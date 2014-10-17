package pl.com.itsense.eventprocessing.provider.rexpression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.com.itsense.eventprocessing.api.Event;
import pl.com.itsense.eventprocessing.api.EventProvider;
import pl.com.itsense.eventprocessing.impl.ProgressProviderImpl;

/**
 * @author ppretki
 */
public class RExpressionProvider extends ProgressProviderImpl implements EventProvider 
{
	/** */
	private String file;
    /** */
    private String timestampexpression;
    /** */
    private String timestampexpressiongroups;
	/** */
	private Pattern timeStampPattern;
	/** */
	private final LinkedList<RExpression> rExpressions = new LinkedList<RExpression>();
	/** */
	private TextLineIterator textLineIterator;
	/** */
	private File inputFile;
	/** */
	public RExpressionProvider()
	{
	}
	
	public void add(final RExpression rExpression)
	{
	    if (!rExpressions.contains(rExpression))
	    {
	        rExpressions.add(rExpression);
	    }
	}
	
	/** */
	public void init()
	{
	    if (timestampexpression != null)
	    {
	        timeStampPattern = Pattern.compile(timestampexpression);
	    }
	    else
	    {
	        timeStampPattern = null;
	    }
	    
	    if (file != null)
	    {
	        inputFile = new File(file);
	        if (!inputFile.exists())
	        {
	            inputFile = null;
	        }
	    }
	}
	

	/**
	 * 
	 * @author ppretki
	 *
	 */
	private class TextLineIterator
	{
		private final Calendar calendar = Calendar.getInstance();
		/** */
		private BufferedReader reader;

		/** */
		private TextLineIterator(final File file)
		{
			try 
			{
				calendar.setTimeInMillis(file.lastModified());
				reader = new BufferedReader(new FileReader(file));
				
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}

		/**
		 * 
		 */
		private long parseDateTimeStamp(final String line)
		{
			final Matcher matcher = timeStampPattern.matcher(line);
			if (matcher.find() && (matcher.groupCount() == 5))
			{
			    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(matcher.group(1)));
			    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(matcher.group(2)));
			    calendar.set(Calendar.MINUTE, Integer.parseInt(matcher.group(3)));
			    calendar.set(Calendar.SECOND, Integer.parseInt(matcher.group(4)));
			    calendar.set(Calendar.MILLISECOND, Integer.parseInt(matcher.group(5)));
			    return calendar.getTimeInMillis();
			}
			return -1;
		}
		
		/**
		 * @throws IOException 
		 * 
		 */
		private RExpressionEvent read()
		{
			String line = null;
			try
			{
				while ((line = reader.readLine()) != null)
				{
                    final long timestamp = parseDateTimeStamp(line);
                    if (timestamp > -1)
                    {
                        for (final RExpression expression : rExpressions)
                        {
                            final RExpressionEvent event = expression.getEvent(timestamp, line);
                            if (event != null)
                            {
                                return event;
                            }
                            
                        }                        
                    }
				}
				closeReader();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}
		
		/** */
		private void closeReader()
		{
			if (reader != null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
				}
				finally
				{
					reader = null;
				}
			}
		}

		/**
		 * 
		 */
		public void remove() 
		{
			throw new UnsupportedOperationException("TextLineInputProvider is read only - file sequantial scaner");
		}
		
	}
	
    @Override
    public Event next(long wait)
    {
        final Event event;
        if (textLineIterator == null && inputFile != null && timeStampPattern != null)
        {
            textLineIterator = new TextLineIterator(inputFile);
        }
        if (textLineIterator != null)
        {
            event = textLineIterator.read();
        }
        else
        {
            event = null;
        }
        return event;
    }
    /**
     * 
     * @return
     */
    public RExpression[] getRExpressions()
    {
        return rExpressions.toArray(new RExpression[0]);
    }
    
    /**
     * 
     * @param file
     */
    public void setFile(final String file)
    {
        this.file = file;
    }
    
    /**
     * 
     * @param timestampexpression
     */
    public void setTimestampexpression(final String timestampexpression)
    {
        this.timestampexpression = timestampexpression;
    }
    
    /**
     * 
     * @param timestampexpressiongroups
     */
    public void setTimestampexpressiongroups(final String timestampexpressiongroups)
    {
        this.timestampexpressiongroups = timestampexpressiongroups;
    }
    /**
     * 
     * @return
     */
    public String getFile()
    {
        return file;
    }
    /**
     * 
     * @return
     */
    public String getTimestampexpression()
    {
        return timestampexpression;
    }
    /**
     * 
     * @return
     */
    public String getTimestampexpressiongroups()
    {
        return timestampexpressiongroups;
    }
}
