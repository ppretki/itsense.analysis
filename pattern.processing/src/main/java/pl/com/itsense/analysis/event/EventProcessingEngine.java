package pl.com.itsense.analysis.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author ppretki
 * 
 */
public class EventProcessingEngine implements EEngine
{
    /** */
    private final LinkedList<Event> events = new LinkedList<Event>();
    /** */
    private Event lastEvent;
    /***/
    private HashMap<StatefulEventProcessor,LinkedList<EventState>> states = new HashMap<StatefulEventProcessor,LinkedList<EventState>>();
    /** */
    private HashMap<String, ArrayList<StatelessEventProcessor>> statelessProcessors = new HashMap<String, ArrayList<StatelessEventProcessor>>();
    /** */
    private HashMap<String, ArrayList<StatefulEventProcessor>> statefullProcessors = new HashMap<String, ArrayList<StatefulEventProcessor>>();
    /** */
    private ArrayList<Report> reports = new ArrayList<Report>();
    /** */
    private HashMap<EEngine.LogLevel, ArrayList<String>> logs = new HashMap<EEngine.LogLevel, ArrayList<String>>();

    /** */
    public void process(final EventProvider[] providers)
    {
        final Event[] currentEvents = new Event[providers.length];
        final Iterator<Event>[] iterators = new Iterator[providers.length];
        for (int i = 0; i < providers.length; i++)
        {
            iterators[i] = providers[i].iterator();
        }
        beginProcessing();
        while (true)
        {
            for (int i = 0; i < currentEvents.length; i++)
            {
                if ((currentEvents[i] == null) && (iterators[i].hasNext()))
                {
                    currentEvents[i] = iterators[i].next();
                }
            }

            Event event = null;
            int eventIndex = -1;
            for (int i = 0; i < currentEvents.length; i++)
            {
                if ((currentEvents[i] != null) && ((event == null) || (event.getTimestamp() > currentEvents[i].getTimestamp())))
                {
                    eventIndex = i;
                    event = currentEvents[i];
                }
            }
            if (event == null)
            {
                break;
            }
            else
            {
                processStatelessProcessors(event);
                processStatefullProcessors(event);
                lastEvent = event;
                events.add(lastEvent);
                currentEvents[eventIndex] = null;

            }
        }
        endProcessing();
        for (final Report report : reports)
        {
            report.create(this);
        }
    }

    /**
     * 
     */
    private void beginProcessing()
    {

    }

    /**
     * 
     */
    private void endProcessing()
    {

    }

    /**
     * 
     */
    private void processStatelessProcessors(final Event event)
    {
        if (statelessProcessors.get(event.getId()) != null)
        {
            for (final StatelessEventProcessor processor : statelessProcessors.get(event.getId()))
            {
                processor.process(event, this);
            }
        }
        if (statefullProcessors.get(event.getId()) != null)
        {
            for (final StatefulEventProcessor processor : statefullProcessors.get(event.getId()))
            {
                final String stateId = processor.process(event, null, this);
                if (stateId != null)
                {
                    LinkedList<EventState> stateList = states.get(processor);
                    if (stateList == null)
                    {
                        stateList = new LinkedList<EventState>();
                        states.put(processor, stateList);
                    }
                    stateList.add(new EventState(event, stateId));
                }
            }
        }
    }
    /**
     * 
     */
    private void processStatefullProcessors(final Event event)
    {
        for (final StatefulEventProcessor processor : statefullProcessors.get(event.getId()))
        {
            final LinkedList<EventState> stateList = states.get(processor);
            if (stateList != null)
            {
                final HashMap<EventState,EventState> transitions = new HashMap<EventState,EventState>();
                for (final EventState state : stateList)
                {
                    if (!state.event.equals(event))
                    {
                        final String stateId = processor.process(event, state.getId(), this);
                        if (!state.getId().equals(stateId))
                        {
                            transitions.put(state, new EventState(event, stateId));
                        }
                        else if (stateId == null)
                        {
                            transitions.put(state, new EventState(event, State.ID_DEACTIVATE));
                        }
                    }
                }
                for (final State prevState : transitions.keySet())
                {
                    stateList.remove(prevState);
                    final EventState nextState = transitions.get(prevState);
                    if (!State.ID_DEACTIVATE.equals(nextState.getId()))
                    {
                        stateList.add(transitions.get(prevState));
                    }
                }
            }
        }
    }

    /**
     * 
     */
    public String[] getEventIds()
    {
        return null;
    }

    /**
     * 
     */
    public LinkedList<Event> getEvents(final String eventId)
    {
        return null;
    }

    /**
     * 
     * @param handler
     */
    public void addEventProcessor(final EventProcessor processor)
    {
        final String values;
        if (processor.getProperty("events") != null)
        {
            values = processor.getProperty("events");
        }
        else
        {
            values = null;
        }

        if (values != null && values.length() > 0)
        {
            for (final String eventId : values.split(","))
            {
                final String trimmedEventId = eventId.trim();
                if (processor instanceof StatefulEventProcessor)
                {
                    ArrayList<StatefulEventProcessor> list = statefullProcessors
                        .get(trimmedEventId);
                    if (list == null)
                    {
                        list = new ArrayList<StatefulEventProcessor>();
                        statefullProcessors.put(trimmedEventId, list);
                    }
                    if (!list.contains(processor))
                    {
                        list.add((StatefulEventProcessor) processor);
                    }
                }
                else if (processor instanceof StatelessEventProcessor)
                {
                    ArrayList<StatelessEventProcessor> list = statelessProcessors
                        .get(trimmedEventId);
                    if (list == null)
                    {
                        list = new ArrayList<StatelessEventProcessor>();
                        statelessProcessors.put(trimmedEventId, list);
                    }
                    if (!list.contains(processor))
                    {
                        list.add((StatelessEventProcessor) processor);
                    }
                }

            }
        }
    }

    /**
     * 
     * @param handler
     */
    public void addReport(final Report report)
    {
        if (!reports.contains(report))
        {
            reports.add(report);
        }
    }

    /**
     * 
     */
    public void log(final String msg, final LogLevel level)
    {
        ArrayList<String> log = logs.get(level);
        if (log == null)
        {
            log = new ArrayList<String>();
            logs.put(level, log);
        }
        log.add(msg);
    }

    /**
     * 
     */
    @Override
    public List<String> getLogs(LogLevel level)
    {
        return logs.get(level);
    }

    @Override
    public Report[] getReports()
    {
        return reports.toArray(new Report[0]);
    }

    @Override
    public Event getEvent()
    {
        return lastEvent;
    }

    @Override
    public Event getEvent(String eventId)
    {
        return null;
    }

    /**
     * 
     * @author P.Pretki
     *
     */
    private class EventState implements State
    {
        /** */
        private EventState prevEvent;
        /** */
        private EventState nextEvent;
        /** */
        private final Event event;
        /** */
        private final String stateId;
        /**
         * 
         */
        public EventState(final Event event, final String stateId)
        {
            this.event = event;
            this.stateId = stateId;
        }
        /**
         * 
         */
        @Override
        public long activationTimestamp()
        {
            return event.getTimestamp();
        }
        /**
         * 
         */
        @Override
        public String getId()
        {
            return stateId;
        }
        /**
         * 
         * @param nextEvent
         */
        public void setNext(final EventState nextEvent)
        {
            this.nextEvent = nextEvent;
        }
        /**
         * 
         * @return
         */
        public EventState getNext()
        {
            return nextEvent;
        }
        /**
         * 
         * @param nextEvent
         */
        public void setPrev(final EventState nextEvent)
        {
            this.prevEvent = nextEvent;
        }
        /**
         * 
         * @return
         */
        public EventState getPrev()
        {
            return prevEvent;
        }

    }
}
