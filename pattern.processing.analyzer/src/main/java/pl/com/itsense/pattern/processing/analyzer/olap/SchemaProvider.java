package pl.com.itsense.pattern.processing.analyzer.olap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author ppretki
 *
 */
public class SchemaProvider 
{
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static StringBuffer loadSchema()
	{
		final StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try
		{
			
			reader = new BufferedReader(new FileReader("/home/ppretki/adb/SequenceAnalyzerOlapSchema.xml"));
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line).append("\n");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		System.out.println(sb.toString());
		return sb;
	}
	
	public static StringBuffer createSchema(final String schemaName)
	{
		final StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>").append("\n");
		sb.append("<Schema name=\"" + schemaName + "\">").append("\n");
		sb.append("<Dimension name=\"Time\" type=\"TimeDimension\">").append("\n");
		sb.append("<Hierarchy hasAll=\"true\" allMemberName=\"All Periods\" primaryKey=\"dateid\">").append("\n");
		sb.append("</Hierarchy>").append("\n");
		sb.append("</Dimension>").append("\n");
		
		sb.append("</Schema>").append("\n");
		return sb;
	}
}
