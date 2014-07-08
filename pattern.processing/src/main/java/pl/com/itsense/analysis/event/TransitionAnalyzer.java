package pl.com.itsense.analysis.event;

/**
 * 
 * @author P.Pretki
 *
 */
public interface TransitionAnalyzer 
{
    public void process(final Transition transition, final EEngine engine);
}
