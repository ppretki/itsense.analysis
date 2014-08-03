package pl.com.itsense.analysis.event;
/**
 * 
 * @author ppretki
 *
 */
public interface ProgressProvider
{
    enum Granularity
    {   
        PERCENT(0.01),
        FIVE_PERCENT(0.05),
        TEN_PERCENT(0.1);
        /** */
        private final double value;
        /***
         * 
         * @param value
         */
        private Granularity(final double value)
        {
            this.value = value;
        }
        /**
         * 
         * @return
         */
        public double getValue()
        {
            return value;
        }
    }
    /**
     * 
     * @param listener
     */
    void add(ProgressListener listener, Granularity granularity);
    /**
     * 
     * @param listener
     */
    void remove(ProgressListener listener);
}
