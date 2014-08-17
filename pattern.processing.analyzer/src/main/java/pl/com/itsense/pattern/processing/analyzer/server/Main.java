package pl.com.itsense.pattern.processing.analyzer.server;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.pivot4j.PivotModel;
import org.pivot4j.datasource.SimpleOlapDataSource;
import org.pivot4j.impl.PivotModelImpl;

import pl.com.itsense.analysis.event.EventProcessingEngine;
import pl.com.itsense.analysis.event.ProgressEvent;
import pl.com.itsense.analysis.event.ProgressListener;
import pl.com.itsense.analysis.event.ProgressProvider.Granularity;
import pl.com.itsense.analysis.event.SequenceConsumer;
import pl.com.itsense.analysis.event.log.LogAnalysis;
import pl.com.itsense.analysis.event.log.configuration.Configuration;
import pl.com.itsense.analysis.event.log.configuration.SequenceConsumerConf;
import pl.com.itsense.analysis.sequence.consumer.DBRecorder;
import pl.com.itsense.pattern.processing.analyzer.BrowserApplication;

public class Main 
{
	private static void olap()
	{
		try 
		{
			Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		final SimpleOlapDataSource dataSource = new SimpleOlapDataSource();
		
		//dataSource.setConnectionString("jdbc:mondrian:Jdbc=jdbc:odbc:MondrianFoodMart;Catalog=FoodMart.xml;");
		dataSource.setConnectionString("jdbc:mondrian:Jdbc=jdbc:postgresql://localhost/adb?user=adb&password=adb;Catalog=FoodMart.xml;");
		
		
		
		String initialMdx = "SELECT {[Measures].[Unit Sales], [Measures].[Store Cost], " 
				+ "[Measures].[Store Sales]} ON COLUMNS, {([Promotion Media].[All Media], "
				+ "[Product].[All Products])} ON ROWS FROM [Sales]";

		PivotModel model = new PivotModelImpl(dataSource);
		model.setMdx(initialMdx);
		model.initialize();
	}
	
	public static void main(final String[] args) 
	{
		olap();
		
        final LogAnalysis logAnalysis = new LogAnalysis(Configuration.parse(new File(args[0])));
        final EventProcessingEngine engine = logAnalysis.getEngine();
        engine.add(new ProgressListener()
        {

			public void change(final ProgressEvent event) 
			{
				System.out.println(event.getProgress());
			}
        }, Granularity.PERCENT);
        final DBRecorder dbRecorder = new DBRecorder();

        final Properties properties = new Properties();
        try 
        {
			//properties.load(Main.class.getResourceAsStream("inMemoryDBRecorder.properties"));
			properties.load(Main.class.getResourceAsStream("PostgresqlDBRecorder.properties"));
		} 
        catch (IOException e1) 
        {
			e1.printStackTrace();
			System.exit(-1);
		}
        for (final Object key :  properties.keySet())
        {
            dbRecorder.setProperty(key.toString(), properties.getProperty(key.toString()));
        }
        final SequenceConsumerConf sequenceConsumerConf = new SequenceConsumerConf();
        sequenceConsumerConf.setSequences(SequenceConsumer.ALL_EVENTS_CONSUMER);
        dbRecorder.configure(sequenceConsumerConf);
        engine.add((SequenceConsumer)dbRecorder);
        System.out.println("Log Analysis = " + logAnalysis);
        logAnalysis.analyze();
		final Server server = new Server(8080);
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setAttribute(BrowserApplication.CTX_ATTRIBUTE_HIBERNATE_SESSION_FACTORY, dbRecorder.getSessionFactory());
        final ServletHolder holder = new ServletHolder(new BrowserApplication.Servlet());
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
