package pl.com.itsense.analysis.event;

/**
 * 
 * @author ppretki
 *
 */
public interface TimestampProducer 
{
	long extract(String line);
}
