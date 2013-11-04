package np.com.axhixh.browsing.history.trident.operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseStateUpdater;
import storm.trident.testing.MemoryMapState;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class PlaceUpdater extends BaseStateUpdater<MemoryMapState> {

    @Override
    public void updateState(MemoryMapState s, List<TridentTuple> list, TridentCollector tc) {
        List<List<String>> keys = new ArrayList<>(list.size());
        List<String> values = new ArrayList<>(list.size());
        for (TridentTuple tt : list) {
            keys.add(Arrays.asList(tt.getString(0)));
            values.add(tt.getString(1));
        }
        s.multiPut(keys, values);
    }
}
