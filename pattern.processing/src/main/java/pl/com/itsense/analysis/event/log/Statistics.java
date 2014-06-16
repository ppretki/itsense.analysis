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
                avg = (avg * count + value)/( count + 1 );  
                count++;
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
}
