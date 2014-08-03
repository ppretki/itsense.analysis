package pl.com.itsense.analysis.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class ProgressProviderImpl implements ProgressProvider
{
    /** */
    private final HashMap<Granularity,LinkedList<ProgressListener>> listeners = new HashMap<Granularity,LinkedList<ProgressListener>>(); 
    /** */
    private final HashMap<Granularity, Double> lastProgresses = new HashMap<ProgressProvider.Granularity, Double>();
    
    {
        for (final Granularity granularity : Granularity.values())
        {
            lastProgresses.put(granularity, 0.0);
        } 
    }
    
    
    @Override
    public void add(final ProgressListener listener, final Granularity granularity)
    {
        LinkedList<ProgressListener> list = listeners.get(granularity);
        if (list == null)
        {
            list = new LinkedList<ProgressListener>();
            listeners.put(granularity, list);
        }
        if (!list.contains(listener))
        {
            list.add(listener);
        }
    }

    @Override
    public void remove(final ProgressListener listener)
    {
        for (final Granularity granularity : listeners.keySet())
        {
            final LinkedList<ProgressListener> list = listeners.get(granularity);
            if ((list != null) && list.contains(listener))
            {
                list.remove(listener);
            }
        }
    }
    
    /**
     * 
     * @param progress
     */
    protected void progressChanged(final double progress)
    {
        for (final Granularity granularity : Granularity.values())
        {
            final LinkedList<ProgressListener> list = listeners.get(granularity);
            if ((list != null) && !list.isEmpty())
            {
                final Double lastprogress = lastProgresses.get(granularity);
                final double actualProgress = Math.floor(progress / granularity.getValue());
//                /System.out.println("granularity = " + granularity + ", progress = " + progress + ", actual = " + actualProgress);
                if (lastprogress != actualProgress)
                {
                    final ProgressEvent event = new ProgressEventImpl(progress); 
                    for (final ProgressListener listener : list)
                    {
                        listener.change(event);
                    }
                    lastProgresses.put(granularity, actualProgress);
                }
            }
            
        }
    }
    
    /**
     * 
     * @author ppretki
     *
     */
    private class ProgressEventImpl implements ProgressEvent
    {
        /** */
        private final double progress;
        /**
         * 
         * @param progress
         */
        public ProgressEventImpl(final double progress)
        {
            this.progress = progress;
        }
        
        @Override
        public double getProgress()
        {
            return progress;
        }

        @Override
        public ProgressProvider getSource()
        {
            return ProgressProviderImpl.this;
        }
        
    }
}
