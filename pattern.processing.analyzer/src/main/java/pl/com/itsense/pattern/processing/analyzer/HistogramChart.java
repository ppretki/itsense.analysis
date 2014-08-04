package pl.com.itsense.pattern.processing.analyzer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource.StreamSource;

/**
 * 
 * @author ppretki
 *
 */
public class HistogramChart implements StreamSource
{
	/** */
	private final double[] values;
	
	/**
	 * 
	 */
	public HistogramChart(final double[] values) 
	{
		this.values = values;
	}

	/**
	 * 
	 */
	public InputStream getStream() 
	{
		final StringBuffer sb = new StringBuffer();
		sb.append("<html>").append("\n");
		return new ByteArrayInputStream(sb.toString().getBytes());
	}
}
