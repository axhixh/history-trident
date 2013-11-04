
package np.com.axhixh.browsing.history.trident.operator;

import backtype.storm.tuple.Values;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseQueryFunction;
import storm.trident.testing.MemoryMapState;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class PlaceQuery extends BaseQueryFunction<MemoryMapState, String> {

    @Override
    public List<String> batchRetrieve(MemoryMapState s, List<TridentTuple> list) {

        List<List<String>> placeIds = new ArrayList<>(list.size());
        for (TridentTuple tuple : list) {
            placeIds.add(Arrays.asList(tuple.getString(0)));
        }
        return s.multiGet(placeIds);
    }

    @Override
    public void execute(TridentTuple tt, String result, TridentCollector tc) {
        if (result != null) {
            String visitDate = tt.getString(1);
            if (visitDate != null) {
                tc.emit(new Values(visitDate));
            }
        }
    }

}
