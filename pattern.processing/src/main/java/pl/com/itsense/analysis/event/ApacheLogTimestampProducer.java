package pl.com.itsense.analysis.event;

import java.util.regex.Pattern;

public class ApacheLogTimestampProducer implements TimestampProducer 
{
	//[23/Mar/2014:21:21:36 +0100]
	final Pattern pattern = Pattern.compile("(\\d{2})/(\\w{3})/(\\d{4}):(\\d{2}):(\\d{2}):(\\d{2})");
	/**
	 * 
	 */
	public long extract(final String line) 
	{
		
		return 0;
	}

}
