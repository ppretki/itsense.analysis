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
        /** */
        private String maxLine = new String();
        /** */
        private String minLine = new String();
        
        /**
         * 
         * @param timestamp
         */
        public void add(final double value, final long timestamp, final String line)
        {
            count++;
            final double  delta = (value - avg);
            avg += delta/count;
            M2  += delta * (value - avg);
            if (value < min)
            {
                min = value;
                maxLine = line;
            }
            if (value > max)
            {
                max = value;
                minLine = line;
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
                if (maxLine != null)
                {
                        sb.append(maxLine).append("\n");
                }
                sb.append("MIN Line:").append("\n");
                if (minLine != null)
                {
                        sb.append(minLine).append("\n");
                }
                return sb.toString();
        }
        
        
        /**
         * 
         * @return
         */
        public double getAvg() {
            return avg;
        }
        
        /**
         * 
         * @return
         */
        public double getMax() {
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
        public String getMaxLine() 
        {
            return maxLine;
        }
        
        /**
         * 
         * @return
         */
        public String getMinLine() 
        {
            return minLine;
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
