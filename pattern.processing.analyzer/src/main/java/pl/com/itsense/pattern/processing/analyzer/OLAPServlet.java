package pl.com.itsense.pattern.processing.analyzer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.com.itsense.pattern.processing.analyzer.olap.SchemaProvider;
/**
 * 
 * @author ppretki
 *
 */
public class OLAPServlet extends HttpServlet 
{
	/** */
	private static final long serialVersionUID = 1L;
	/** */
	private final String CATALOG = "catalog.xml";
	/**
	 * 
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{
		final String URI = request.getRequestURI();
		System.out.println("URI = " + URI);
		if (URI.endsWith(CATALOG))
		{
			response.setContentType("text/xml");
			final StringBuffer sb = SchemaProvider.loadSchema();
			response.getOutputStream().print(sb.toString());
			response.getOutputStream().flush();
		}
	}
}
