package pl.com.itsense.analysis.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import pl.com.itsense.analysis.event.log.configuration.SequenceConf;
import pl.com.itsense.analysis.event.log.configuration.TermConf;

/**
 * 
 * @author P.Pretki
 *
 */
public class SequenceFactory
{
    /** */
    private HashMap<String, SequenceConf> sequenceConfs = new HashMap<String, SequenceConf>();
    /** */
    private HashMap<String, ArrayList<Sequence>> prototypes = new HashMap<String, ArrayList<Sequence>>(); 
    /**
     * 
     * @param sequenceConfs
     */
    public void setSequances(final ArrayList<SequenceConf> sequanceConfs)
    {
        this.sequenceConfs.clear();
        for (final SequenceConf sequenceConf : sequanceConfs)
        {
            this.sequenceConfs.put(sequenceConf.getId(), sequenceConf);
        }
        prepare();
    }
    /**
     * 
     * @param event
     * @return
     */
    public List<Sequence> getSequance(final Event event)
    {
        List<Sequence> sequence = null;
        if (prototypes != null)
        {
            final ArrayList<Sequence> list = prototypes.get(event.getId());
            if (list != null)
            {
                for (final Sequence seq : list)
                {
                    if (seq.accept(event))
                    {
                        if (sequence == null)
                        {
                            sequence = new ArrayList<Sequence>();
                        }
                        sequence.add(seq);
                    }
                }
            }
            if (sequence != null)
            {
                for (final Sequence seq : sequence)
                {
                    list.remove(seq);
                    list.add(new Sequence(seq.getTerms(), sequenceConfs.get(seq.getId()).getMeasures(), seq.getName() , seq.getId()));
                }
            }
        }
        return sequence;
    }
    /**
     * 
     */
    private void prepare()
    {
        if (sequenceConfs != null)
        {
            prototypes.clear();
            for (final SequenceConf sequenceConf : sequenceConfs.values())
            {
                final List<TermConf> terms = sequenceConf.getTerms();
                if (terms != null)
                {
                    final ArrayList<Integer> indexes = new ArrayList<Integer>();
                    final HashMap<Integer,TermConf> map = new HashMap<Integer,TermConf>();
                    for (final TermConf term : terms)
                    {
                        try
                        {
                            final int index = Integer.parseInt(term.getIndex().trim());
                            indexes.add(index);
                            map.put(index, term);
                        }
                        catch(NumberFormatException e)
                        {
                        }
                    }
                    
                    if (!indexes.isEmpty())
                    {
                        if (indexes.size() > 1) Collections.sort(indexes);
                        
                        final Term[] termTable = new Term[indexes.size()];
                        for (int i = 0 ; i < indexes.size(); i++)
                        {
                            final TermConf term = map.get(indexes.get(i));
                            termTable[i] = new Term(term);
                        }
                        
                        final Sequence sequence = new Sequence(termTable, sequenceConf.getMeasures(), sequenceConf.getName(), sequenceConf.getId());
                        final String eventId = termTable[0].getEventId();
                        if (eventId != null)
                        {
                            ArrayList<Sequence> list = prototypes.get(eventId);
                            if (list == null)
                            {
                                list = new ArrayList<Sequence>();
                                prototypes.put(eventId, list);
                            }
                            list.add(sequence);
                        }
                    }
                }
            }
        }
    }
    
}
