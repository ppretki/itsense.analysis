package pl.com.itsense.pattern.processing.analyzer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import com.vaadin.server.StreamResource.StreamSource;

/**
 * 
 * @author ppretki
 *
 */
public class LineChart implements StreamSource
{
	/** */
	private static final long serialVersionUID = 1L;
	/** */
	private final List<Double> values;
	/** */
	private final List<Long> timestamps;
	/**
	 * 
	 */
	public LineChart(final List<Long> timestamps, final List<Double> values) 
	{
		this.values = values;
		this.timestamps = timestamps;
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
		sb.append("		google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});").append("\n");
		sb.append("		google.setOnLoadCallback(drawChart);").append("\n");
		
		sb.append("		function drawChart() {").append("\n");
		
		sb.append("		var data = google.visualization.arrayToDataTable([").append("\n");
		sb.append("		['time', 'values'],").append("\n");
		for (int i = 0 ; i < values.size(); i++)
		{
			sb.append("		[new Date("+timestamps.get(i)+"), "+values.get(i)+"],").append("\n");
		}
		sb.append("		]);").append("\n");
		
		sb.append("		var options = {title: 'Sequances', legend: { position: 'none' }, };").append("\n");
		
		sb.append("		var chart = new google.visualization.LineChart(document.getElementById('chart_div'));").append("\n");
		sb.append("		chart.draw(data, options);").append("\n");
		
		sb.append("		}").append("\n");
		
		
		sb.append("</script>").append("\n");
		sb.append("</head>").append("\n");
		
		sb.append("<body>").append("\n");
		sb.append("<div id=\"chart_div\" style=\"width: 100%; height: 100%;\"></div>").append("\n");
		sb.append("</body>").append("\n");		
		sb.append("</html>").append("\n");
		return new ByteArrayInputStream(sb.toString().getBytes());
	}
}
