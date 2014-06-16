package pl.com.itsense.analysis.event;

import pl.com.itsense.analysis.event.log.HandlerParamConf;

/**
 * 
 * @author ppretki
 *
 */
public interface EventProcessingHandler 
{
	/** */
	void processEvent(Event event, EEngine engine);
	/** */
	void init(HandlerParamConf[] params);
}
