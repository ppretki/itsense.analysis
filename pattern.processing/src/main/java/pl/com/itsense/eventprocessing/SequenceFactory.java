package pl.com.itsense.eventprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.com.itsense.eventprocessing.api.Event;
import pl.com.itsense.eventprocessing.api.Member;
import pl.com.itsense.eventprocessing.api.Sequence;
import pl.com.itsense.eventprocessing.configuration.MemberConf;
import pl.com.itsense.eventprocessing.configuration.SequenceConf;

/**
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
    public void setSequances(final List<SequenceConf> sequanceConfs)
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
                    list.add(new SequenceImpl(seq.getTerms(), sequenceConfs.get(seq.getId()).getMeasures(), seq.getName() , seq.getId()));
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
                final List<MemberConf> terms = sequenceConf.getTerms();
                if (terms != null)
                {
                        final Member[] termTable = new Member[terms.size()];
                        for (int i = 0; i < terms.size(); i++)
                        {
                            termTable[i] = new MemberImpl(terms.get(i));
                        }
                        final Sequence sequence = new SequenceImpl(termTable, sequenceConf.getMeasures(), sequenceConf.getName(), sequenceConf.getId());
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
