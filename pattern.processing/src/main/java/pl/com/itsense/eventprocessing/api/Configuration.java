package pl.com.itsense.eventprocessing.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import pl.com.itsense.eventprocessing.configuration.EventConf;
import pl.com.itsense.eventprocessing.configuration.EventConsumerConf;
import pl.com.itsense.eventprocessing.configuration.FileConf;
import pl.com.itsense.eventprocessing.configuration.ReportConf;
import pl.com.itsense.eventprocessing.configuration.SequenceConf;
import pl.com.itsense.eventprocessing.configuration.SequenceConsumerConf;
/**
 * 
 * @author ppretki
 *
 */
public interface Configuration
{
    /**
     * 
     * @return
     */
    List<EventConf> getEvents();

    /**
     * 
     * @return
     */
    FileConf getFile();


  
    List<SequenceConf> getSequences();
    /**
     * 
     * @return
     */
    List<EventConsumerConf> getEventConsumers();
    
    /**
     * 
     * @return
     */
    List<SequenceConsumerConf> getSequenceConsumers();
    
    /**
     * 
     * @return
     */
    List<ReportConf> getReports();
}
