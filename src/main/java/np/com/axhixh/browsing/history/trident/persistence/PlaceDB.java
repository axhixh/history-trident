package np.com.axhixh.browsing.history.trident.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import storm.trident.state.State;

/**
 *
 * @author ashish
 */
public class PlaceDB implements State {

    private final Map<String, String> db = new ConcurrentHashMap<>();
    
    @Override
    public void beginCommit(Long txid) {
        
    }

    @Override
    public void commit(Long txid) {
        
    }
    
    public void putAll(Map<String, String> entries) {
        db.putAll(entries);
    }
    
    public List<String> getAll(List<String> ids) {
        List<String> places = new ArrayList<>(ids.size());
        for (String id : ids) {
            places.add(db.get(id));
        }
        return places;
    }
    
}
