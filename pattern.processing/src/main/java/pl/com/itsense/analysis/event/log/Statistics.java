package pl.com.itsense.analysis.event.log;

/**
 * 
 * @author ppretki
 *
 */
public class Statistics 
{
        /** */
        private int count;
        /** */
        private double avg;
        /** http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance */
        private double M2;
        /** */
        private double min = Double.MAX_VALUE;
        /** */
        private double max= Double.MIN_VALUE;
        
        /**
         * 
         * @param timestamp
         */
        public void add(final double value, final long timestamp)
        {
            count++;
            final double  delta = (value - avg);
            avg += delta/count;
            M2  += delta * (value - avg);
            if (value < min)
            {
                min = value;
            }
            if (value > max)
            {
                max = value;
            }
        }
        
        /**
         * 
         */
        @Override
        public String toString() 
        {
                final StringBuffer sb = new StringBuffer();
                sb.append("count = " + count + ", AVG = " + avg + ", MAX = " +  max + ", MIN = " +  min).append("\n");
                sb.append("MAX Line:").append("\n");
                sb.append("MIN Line:").append("\n");
                return sb.toString();
        }
        
        
        /**
         * 
         * @return
         */
        public double getAvg() 
        {
            return avg;
        }
        
        /**
         * 
         * @return
         */
        public double getMax() 
        {
            return max;
        }
        
        /**
         * 
         * @return
         */
        public int getCount() 
        {
            return count;
        }
        
        /**
         * 
         * @return
         */
        public double getMin() 
        {
            return min;
        }
        
        
        /**
         * 
         * @return
         */
        public double getStd() 
        {
            return count > 1 ? Math.sqrt(M2/(count-1)) : 0.0;
        }
}
