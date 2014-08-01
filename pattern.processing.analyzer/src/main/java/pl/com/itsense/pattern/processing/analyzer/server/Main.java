package pl.com.itsense.pattern.processing.analyzer.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.vaadin.terminal.gwt.server.ApplicationServlet;

public class Main 
{

	public static void main(final String[] args) 
	{
		final Server server = new Server(8080);
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        final ServletHolder holder = new ServletHolder(new ApplicationServlet());
        holder.setInitParameter("application", "pl.com.itsense.pattern.processing.analyzer.MyVaadinApplication");
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
