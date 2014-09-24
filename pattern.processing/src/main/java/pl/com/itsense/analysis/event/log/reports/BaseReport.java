package pl.com.itsense.analysis.event.log.reports;

import pl.com.itsense.eventprocessing.PropertyHolderImpl;
import pl.com.itsense.eventprocessing.api.EventProcessingEngine;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycle;
import pl.com.itsense.eventprocessing.api.Report;

/**
 * 
 */
public abstract class BaseReport extends PropertyHolderImpl implements Report
{
    @Override
    public void enter(final ProcessingLifecycle lifecycle, final EventProcessingEngine engine)
    {
        if (ProcessingLifecycle.FINISH.equals(lifecycle))
        {
            create(engine);
        }
    }
}
