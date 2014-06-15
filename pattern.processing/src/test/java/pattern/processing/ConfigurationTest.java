package pattern.processing;


/**
 * 
 */
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import pl.com.itsense.analysis.event.log.Configuration;
import pl.com.itsense.analysis.event.log.EventConf;
import pl.com.itsense.analysis.event.log.FileConf;

public class ConfigurationTest 
{

	@Test
	public void testParse() 
	{
		final URL url = getClass().getResource("/config.xml");
		try 
		{
			final Configuration configuration = Configuration.parse(new File(url.toURI()));
			for (final EventConf event : configuration.getEvents())
			{
				System.out.println(event);
			}

			for (final FileConf file : configuration.getFiles())
			{
				System.out.println(file);
			}
			
			
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
		
		
	}

}
