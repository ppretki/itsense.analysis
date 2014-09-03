package pl.com.itsense.analysis.event.log.reports;

import pl.com.itsense.analysis.event.EEngine;
import pl.com.itsense.analysis.event.EEngine.ProcessingLifecycle;
import pl.com.itsense.analysis.event.PropertyHolderImpl;
import pl.com.itsense.analysis.event.Report;

/**
 * 
 * @author P.Pretki
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
