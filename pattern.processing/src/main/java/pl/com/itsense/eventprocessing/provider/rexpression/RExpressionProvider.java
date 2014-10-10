package pl.com.itsense.eventprocessing.provider.rexpression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
	private void init()
	{
	    
	    
		for (final EventConf event : events)
		{
			final List<PatternConf> list = event.getPatterns();
			if (!list.isEmpty())
			{
				int i = 0;
				final Pattern[] p = new Pattern[list.size()]; 
				for (final PatternConf pattern : list)
				{
					final String expression = pattern.getRegExp() == null ? pattern.getValue() : pattern.getRegExp();
					p[i] = Pattern.compile(expression);
					patternDefs.put(p[i], pattern);
					i++;
				}
				patterns.put(event, p);
			}
		}
	}

	/**
	 * 
	 * @author ppretki
	 *
	 */
	private class TextLineIterator implements Iterator<Event>
	{
		private final Calendar calendar = Calendar.getInstance();
		/** */
		private BufferedReader reader;
		/** */
		private TextLine nextEvent;
		/** */
		private long filePos;

		/** */
		private TextLineIterator(final File file, final Pattern timestampPattern, final HashMap<Integer,Integer> calendarMappings)
		{
			try 
			{
			    filePos = 0;
				calendar.setTimeInMillis(file.lastModified());
				reader = new BufferedReader(new FileReader(file));
				
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
		
		
		/** */
		public boolean hasNext() 
		{
			if ((nextEvent == null) && (reader != null))
			{
				nextEvent = read();
			}
			return nextEvent != null;
		}

		
		public Event next() 
		{
			Event event = null;
			if (nextEvent != null)
			{
				event = nextEvent;
				nextEvent = null;
			}
			else
			{
				event = read();
			}
			return event;
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
		private TextLine read()
		{
			String line = null;
			try
			{
				while ((line = reader.readLine()) != null)
				{
				    lineCounter++;
				    filePos += line.getBytes().length;
				    progressChanged((double)filePos/(double)file.length());
				    
                    if (lineCounter >= top)
                    {
                        break;
                    }
					
					for (final EventConf event : patterns.keySet())
					{
						
						for (final Pattern pattern : patterns.get(event))
						{
							final Matcher matcher = pattern.matcher(line);
							if (matcher.find())
							{
							    
								final long timestamp = parseDateTimeStamp(line);
								if (timestamp > -1)
								{
									final TextLine textLineEvent = new TextLine(event.getId(), timestamp);
									textLineEvent.setProperty(Event.PROPERTY_LINE, line);
									if (matcher.groupCount() > 0)
									{
										final PatternConf patternConf = patternDefs.get(pattern);
										for (int i = 1 ; i < (matcher.groupCount()+1); i++)
										{
											textLineEvent.setProperty(patternConf.getId() + "$" + i, matcher.group(i));
										}
									}
									return textLineEvent; 	
								}
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
        return null;
    }

    @Override
    public <T extends Event> Class<T>[] getEventClasses()
    {
        return null;
    }
}
