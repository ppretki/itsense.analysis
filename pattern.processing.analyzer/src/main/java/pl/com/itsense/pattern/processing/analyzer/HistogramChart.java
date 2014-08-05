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
	private static final long serialVersionUID = 1L;
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
		
		sb.append("<head>").append("\n");
		sb.append("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>").append("\n");
		sb.append("<script type=\"text/javascript\">").append("\n");
		sb.append("</script>").append("\n");
		sb.append("</head>").append("\n");
		
		sb.append("<body>").append("\n");
		sb.append("<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>").append("\n");
		sb.append("</body>").append("\n");		
		sb.append("</html>").append("\n");
		return new ByteArrayInputStream(sb.toString().getBytes());
	}
}
