package pl.com.itsense.analysis.event.log;

import java.util.LinkedList;

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
        private LinkedList<String> maxLines = new LinkedList<String>();
        /** */
        private LinkedList<String> minLines = new LinkedList<String>();
        
        /**
         * 
         * @param timestamp
         */
        public void add(final double value, final long timestamp, final LinkedList<String> lines)
        {
                avg = (avg * count + value)/( count + 1 );  
                count++;
                if (value < min)
                {
                        min = value;
                        if ((lines != null) && (lines.size() > 0))
                        {
                            minLines.clear();
                            minLines.addAll(lines);
                        }
                }
                if (value > max)
                {
                        max = value;
                        if ((lines != null) && (lines.size() > 0))
                        {
                            maxLines.clear();
                            maxLines.addAll(lines);
                        }
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
                for (final String line : maxLines)
                {
                        sb.append(line).append("\n");
                }
                sb.append("MIN Line:").append("\n").append(minLines).append("\n");
                for (final String line : minLines)
                {
                        sb.append(line).append("\n");
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
}
