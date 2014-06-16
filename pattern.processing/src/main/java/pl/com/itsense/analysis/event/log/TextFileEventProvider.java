package pl.com.itsense.analysis.event.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.com.itsense.analysis.event.Event;
import pl.com.itsense.analysis.event.EventProvider;

/**
 * 
 * @author ppretki
 *
 */
public class TextFileEventProvider implements EventProvider 
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
	public TextFileEventProvider(final File file, final EventConf[] events)
	{
		this.file = file;
		this.events = events;
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
		private TextLineIterator(final File file)
		{
			try 
			{
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
					for (final EventConf event : events)
					{
						for (final PatternConf pattern : event.getPatterns())
						{
							final int index = line.indexOf(pattern.getValue());
							if (index > -1)
							{
								final long timestamp = parseDateTimeStamp(line);
								if (timestamp > -1)
								{
									return new TextLine(event.getId(), timestamp); 	
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
		private class TextLine implements Event
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
			
		}
		
	}
}
