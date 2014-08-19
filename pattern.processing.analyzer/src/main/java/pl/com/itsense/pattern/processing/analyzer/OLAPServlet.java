package pl.com.itsense.pattern.processing.analyzer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author ppretki
 *
 */
public class OLAPServlet extends HttpServlet 
{
	private final String CATALOG = "catalog.xml";
	/**
	 * 
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final String URI = request.getRequestURI();
		if (URI.endsWith(CATALOG))
		{
			response.setContentType("text/xml");
			final StringBuffer sb = new StringBuffer();
			
			response.getOutputStream().print(sb.toString());
			response.getOutputStream().flush();
		}
	}
}
