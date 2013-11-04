package np.com.axhixh.browsing.history.trident.persistence;

import backtype.storm.task.IMetricsContext;
import java.util.Map;
import storm.trident.state.State;
import storm.trident.state.StateFactory;

/**
 *
 * @author ashish
 */
public class PlaceDBFactory implements StateFactory {

    @Override
    public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
        return new PlaceDB();
    }
    
}
