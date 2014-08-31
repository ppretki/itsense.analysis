package pl.com.itsense.pattern.processing.analyzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.SQLException;

import org.olap4j.OlapDataSource;
import org.olap4j.OlapException;
import org.olap4j.metadata.Cube;
import org.pivot4j.PivotModel;
import org.pivot4j.datasource.SimpleOlapDataSource;
import org.pivot4j.impl.PivotModelImpl;
import org.pivot4j.ui.html.HtmlRenderCallback;
import org.pivot4j.ui.table.TableRenderer;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author ppretki
 *
 */
public class PivotTableTab extends VerticalLayout 
{
	static
	{
		try 
		{
			Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	private final static String CONNECTION_MONDRIAN_STRING = "jdbc:mondrian:Jdbc=jdbc:postgresql://localhost/adb?user=adb&password=adb;Catalog=file:///home/ppretki/adb/SequenceAnalyzerOlapSchema.xml;";

	/** */
	//private final static String CONNECTION_MONDRIAN_STRING = "jdbc:mondrian:Jdbc=jdbc:postgresql://localhost/adb?user=adb&password=adb;Catalog=http://localhost:8080/olap/catalog.xml;";
	/** */
	private final static String INITIAL_MDX_QUERY = "SELECT {[SEQUENCE].[SEQUANCE_ID].MEMBERS} ON COLUMNS, {[Time].[Hour].MEMBERS} ON ROWS FROM [SequenceAnalyzerCube]";
	/** */
	private final PivotModel pivotModel;
	/** */
	private final TextField editor = new TextField("MDX QUERY:");
	/** */
	private final StreamSource streamSource = new StreamSource() 
	{
		public InputStream getStream() 
		{
			System.out.println("getStream");
			pivotModel.getCellSet();
			final StringWriter writer = new StringWriter();
			TableRenderer renderer = new TableRenderer();
			renderer.setShowDimensionTitle(false); // Optionally hide the dimension title headers.
			renderer.setShowParentMembers(true); // Optionally make the parent members visible.
			renderer.render(pivotModel, new HtmlRenderCallback(writer)); // Render the result as a HTML page.
			writer.flush();
			try 
			{
				writer.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			return new ByteArrayInputStream(writer.getBuffer().toString().getBytes());
		}
	}; 
	private final StreamResource resultTableStreamResource = new StreamResource(streamSource, "mdxResults.html");
	/** */
	private Embedded htmlEmbededTable;
	/**
	 * 
	 */
	public PivotTableTab() 
	{
		final SimpleOlapDataSource dataSource = new SimpleOlapDataSource();
		dataSource.setConnectionString(CONNECTION_MONDRIAN_STRING);
		pivotModel = new PivotModelImpl(dataSource);
		pivotModel.setMdx(INITIAL_MDX_QUERY);
		pivotModel.initialize();
		pivotModel.getCellSet();
		buildUI();
	}
	/**
	 *
	 */
	private void buildUI() 
	{
		editor.setWidth(100, Unit.PERCENTAGE);
		editor.setValue(INITIAL_MDX_QUERY);
		editor.setImmediate(true);
		editor.addValueChangeListener(new ValueChangeListener() 
		{
			public void valueChange(final ValueChangeEvent event) 
			{
				System.out.println("valueChange");
				pivotModel.setMdx(editor.getValue());
				htmlEmbededTable.markAsDirty();
			}
		} ); 
		resultTableStreamResource.setCacheTime(0L);
		htmlEmbededTable = new Embedded("MDX RESULTS", new StreamResource(streamSource, "mdxResults.html"));
		htmlEmbededTable.setWidth(100, Unit.PERCENTAGE);
		
		addComponent(editor);
		addComponent(htmlEmbededTable);
	}
	
	
	
}
