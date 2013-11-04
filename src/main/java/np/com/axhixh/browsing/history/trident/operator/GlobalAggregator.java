
package np.com.axhixh.browsing.history.trident.operator;

import java.util.Map;
import np.com.axhixh.browsing.history.trident.persistence.MockDB;
import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class GlobalAggregator extends BaseFilter {

    @Override
    public boolean isKeep(TridentTuple tt) {
        Object obj = tt.get(0);
        if (obj instanceof Map) {
            Map m = (Map) obj;
            if (!m.isEmpty()) {
                MockDB.getInstance().putAll(m);
                return true;
            }
        }
        return false;
    }

}
