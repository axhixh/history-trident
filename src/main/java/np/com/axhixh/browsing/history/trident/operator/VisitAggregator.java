
package np.com.axhixh.browsing.history.trident.operator;

import backtype.storm.tuple.Values;
import java.util.HashMap;
import java.util.Map;
import storm.trident.operation.BaseAggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class VisitAggregator extends BaseAggregator<Map<String, Integer>> {

    @Override
    public Map<String, Integer> init(Object o, TridentCollector tc) {
        return new HashMap<>();
    }

    @Override
    public void aggregate(Map<String, Integer> t, TridentTuple tt, TridentCollector tc) {
        int dow = tt.getInteger(0);
        int hr = tt.getInteger(1);
        String key = String.format("%d:%02d", dow, hr);
        Integer val = t.get(key);
        if (val == null) {
            val = 0;
        }
        t.put(key, val + 1);
    }

    @Override
    public void complete(Map<String, Integer> t, TridentCollector tc) {
        if (!t.isEmpty()) {
            tc.emit(new Values(t));
        }
    }

}
