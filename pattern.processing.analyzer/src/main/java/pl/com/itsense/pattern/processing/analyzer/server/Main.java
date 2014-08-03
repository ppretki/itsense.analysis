package pl.com.itsense.pattern.processing.analyzer.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pl.com.itsense.pattern.processing.analyzer.BrowserApplication;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

public class Main 
{
	public static void main(final String[] args) 
	{
		final Server server = new Server(8080);
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setInitParameter(VaadinSession.UI_PARAMETER, BrowserApplication.class.getName());
        final ServletHolder holder = new ServletHolder(new VaadinServlet());
        //holder.setInitParameter("application", "pl.com.itsense.pattern.processing.analyzer.BrowserApplication");
        holder.setInitParameter("config", args[0]);
        context.addServlet(holder,"/*");
        server.setHandler(context);
        try 
        {
			server.start();
			server.join();
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
        

		
	}

}
