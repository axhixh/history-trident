package np.com.axhixh.browsing.history.trident.operator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import np.com.axhixh.browsing.history.trident.persistence.PlaceDB;
import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseStateUpdater;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class PlaceUpdater extends BaseStateUpdater<PlaceDB> {

    @Override
    public void updateState(PlaceDB db, List<TridentTuple> list, TridentCollector tc) {
        Map<String, String> entries = new HashMap<>();
        for (TridentTuple tt : list) {
            entries.put(tt.getString(0), tt.getString(1));
        }
        db.putAll(entries);
    }
}
