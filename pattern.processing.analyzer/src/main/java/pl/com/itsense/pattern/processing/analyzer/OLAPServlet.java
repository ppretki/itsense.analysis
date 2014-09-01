package pl.com.itsense.pattern.processing.analyzer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
	/** */
	private final String CSS_FILE = "olap.css";
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
		else if (URI.endsWith(CSS_FILE))
		{
			response.setContentType("text/css");
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			final InputStream input = getClass().getResourceAsStream(CSS_FILE);
			final byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = input.read(buffer)) > -1)
			{
				output.write(buffer,0, len);
			}
			response.getOutputStream().write(output.toByteArray());
			response.getOutputStream().flush();
		}
	}
}
