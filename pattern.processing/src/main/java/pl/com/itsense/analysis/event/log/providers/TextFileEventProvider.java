package pl.com.itsense.analysis.event.log.providers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.EventProvider;
import pl.com.itsense.analysis.event.ProgressProviderImpl;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.log.configuration.EventConf;
import pl.com.itsense.analysis.event.log.configuration.PatternConf;

/**
 * 
 * 
 * TODO: add events to dispatch queue - single line may produce multiple events. 
 * @author ppretki
 *
 */
public class TextFileEventProvider extends ProgressProviderImpl implements EventProvider 
{
	/** */
	private final File file;
	//16-11:32:58.816
	/** */
	private final Pattern timeStampPattern = Pattern.compile("^(\\d{2})-(\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})");
	/** */
	private Iterator<Event> iterator;
	/** */
	private final EventConf[] events;
	/** */
	private String from;
	/** */
	private final int top;
	/** */
	private int lineCounter;
	/** */
	private HashMap<EventConf,Pattern[]> patterns = new HashMap<EventConf,Pattern[]>();   
	/** */
	private HashMap<Pattern, PatternConf> patternDefs = new HashMap<Pattern,PatternConf>();
	
	/** */
	public TextFileEventProvider(final File file, final EventConf[] events, final String from, final int top)
	{
		this.file = file;
		this.events = events;
		this.from = from;
		this.top = top;
		init();
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
	
	/** */
	public Iterator<Event> iterator() 
	{
		if ((iterator == null) && (file != null) && (file.exists()))
		{
			iterator = new TextLineIterator(file);
		}
		return iterator;
	}
	
	/**
	 * 
	 */
	public String[] getEventIds() 
	{
	    final String[] ids = new String[events.length];
	    for (int i = 0 ; i < events.length ; i++)
	    {
	        ids[i] = events[i].getId();
	    }
	    return ids;
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
		private TextLineIterator(final File file)
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
				    
					if (from != null)
				    {
						if (line.contains(from))
				        {
							from = null; 
				        }
				        continue;
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
		
		/**
		 * 
		 * @author ppretki
		 *
		 */
		private class TextLine extends PropertyHolderImpl implements Event
		{
			/** */
			private final long timestamp;
			/** */
			private final String eventId;
			/**
			 * 
			 */
			public TextLine(final String eventId, final long timestamp) 
			{
				this.eventId = eventId;
				this.timestamp = timestamp;
			}
			
			/** */
			public long getTimestamp() 
			{
				return timestamp;
			}
			/**
			 * 
			 */
			public String getId() 
			{
				return eventId;
			}
			
			/**
			 * 
			 */
			@Override
			public String getProperty(String name) 
			{
				return super.getProperty(name);
			}
			
			@Override
			public String toString() 
			{
				return "TextLineEvent: id = " + eventId + ", timestamp = " + timestamp + "\n " + super.toString();
			}
			
			
		}
		
	}
}
