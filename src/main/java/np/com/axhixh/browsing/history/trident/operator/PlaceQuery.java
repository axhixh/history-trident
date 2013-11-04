
package np.com.axhixh.browsing.history.trident.operator;

import backtype.storm.tuple.Values;
import java.util.ArrayList;
import java.util.List;
import np.com.axhixh.browsing.history.trident.persistence.PlaceDB;
import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseQueryFunction;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class PlaceQuery extends BaseQueryFunction<PlaceDB, String> {

    @Override
    public List<String> batchRetrieve(PlaceDB db, List<TridentTuple> list) {
        List<String> ids = new ArrayList<>(list.size());
        for (TridentTuple tuple : list) {
            String id = tuple.getString(0);
            ids.add(id);
        }
        return db.getAll(ids);
    }

    @Override
    public void execute(TridentTuple tt, String result, TridentCollector tc) {
        // Output visit_date as vdate only if we can find a place for the given
        // place_id
        if (result != null) {
            tc.emit(new Values(tt.getString(1)));
        }
    }

}
