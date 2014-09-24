package pl.com.itsense.analysis.event.log.reports;

import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.eventprocessing.api.EEngine;
import pl.com.itsense.eventprocessing.api.ProcessingLifecycle;
import pl.com.itsense.eventprocessing.api.Report;

/**
 * 
 */
public abstract class BaseReport extends PropertyHolderImpl implements Report
{
    @Override
    public void enter(final ProcessingLifecycle lifecycle, final EEngine engine)
    {
        if (ProcessingLifecycle.FINISH.equals(lifecycle))
        {
            create(engine);
        }
    }
}
