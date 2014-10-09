package pl.com.itsense.pattern.processing.analyzer.server;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import org.olap4j.Position;

import mondrian.web.servlet.MdxQueryServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.pivot4j.PivotModel;
import org.pivot4j.datasource.SimpleOlapDataSource;
import org.pivot4j.impl.PivotModelImpl;
import org.pivot4j.ui.html.HtmlRenderCallback;
import org.pivot4j.ui.table.TableRenderer;

import pl.com.itsense.eventprocessing.Main;
import pl.com.itsense.eventprocessing.ProgressEvent;
import pl.com.itsense.eventprocessing.ProgressListener;
import pl.com.itsense.eventprocessing.SequenceConsumer;
import pl.com.itsense.eventprocessing.ProgressProvider.Granularity;
import pl.com.itsense.eventprocessing.consumer.DBRecorder;
import pl.com.itsense.eventprocessing.impl.EventProcessingEngineImpl;
import pl.com.itsense.eventprocessing.xml.Configuration;
import pl.com.itsense.eventprocessing.xml.SequenceConsumerConf;
import pl.com.itsense.pattern.processing.analyzer.BrowserApplication;
import pl.com.itsense.pattern.processing.analyzer.OLAPServlet;

public class Main 
{
	private final static String CONNECTION_MONDRIAN_STRING = "jdbc:mondrian:Jdbc=jdbc:postgresql://localhost/adb?user=adb&password=adb;Catalog=file:///home/ppretki/adb/SequenceAnalyzerOlapSchema.xml;";

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
		final String mdxQuery = "SELECT {[SEQUENCE].[SEQUANCE_ID].MEMBERS} ON COLUMNS, {[Time].[Hour].MEMBERS} ON ROWS FROM [SequenceAnalyzerCube]";
		final SimpleOlapDataSource dataSource = new SimpleOlapDataSource();
		dataSource.setConnectionString(CONNECTION_MONDRIAN_STRING);
		PivotModel pModel = new PivotModelImpl(dataSource);
		pModel.setMdx(mdxQuery);
		pModel.initialize();
		
		CellSet cellSet = pModel.getCellSet();

		// Axes of the resulting query.
		List<CellSetAxis> axes = cellSet.getAxes();

		// The COLUMNS axis
		CellSetAxis columns = axes.get(0);

		// The ROWS axis
		CellSetAxis rows = axes.get(1);

		// Member positions of the ROWS axis.
		List<Position> positions = rows.getPositions();
		
		
		final StringWriter writer = new StringWriter();
		TableRenderer renderer = new TableRenderer();
		renderer.setShowDimensionTitle(false); // Optionally hide the dimension title headers.
		renderer.setShowParentMembers(true); // Optionally make the parent members visible.
		renderer.render(pModel, new HtmlRenderCallback(writer)); // Render the result as a HTML page.

		writer.flush();
		try 
		{
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		System.out.println(writer.getBuffer().toString());
		
		
	}

	public static void main(final String[] args) 
	{
		
        final Main logAnalysis = new Main(Configuration.parse(new File(args[0])));
        final EventProcessingEngineImpl engine = logAnalysis.getEngine();
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
        
        final ServletHolder olapServletHolder = new ServletHolder(new OLAPServlet());
        context.addServlet(olapServletHolder,"/olap/*");
        
        server.setHandler(context);
        
        try 
        {
			server.start();
			//olap();
			server.join();
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
        

		
	}

}
