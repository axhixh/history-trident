
package np.com.axhixh.browsing.history.trident.persistence;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ashish
 */
public class MockDB {
    private final Map<String, Integer> db;
    private static MockDB instance;
    
    private MockDB() {
        db = new HashMap();
    }
    public synchronized static MockDB getInstance() {
        if (instance == null) {
            instance = new MockDB();
        }
        return instance;
    }
    public synchronized void putAll(Map<String, Integer> entries) {
        for (Map.Entry<String, Integer> entry : entries.entrySet()) {
            String key = entry.getKey();
            Integer v = db.get(key);
            if (v == null) {
                v = 0;
            }
            db.put(key, v + entry.getValue());
        }
    }    
    
    public Integer get(String key) {
        return db.get(key);
    }
    
    public void dump(PrintStream out) {
        for (int dow = 1; dow <= 7; dow++) {
            for (int hour = 0; hour < 24; hour++) {
                String key = String.format("%d:%02d", dow, hour);
                Integer value = db.get(key);
                if (value == null) {
                    value = 0;
                }
                out.println(String.format("%d,%d,%d", dow, hour, value));
            }
        }
    }

    public void dumpForPlot(PrintStream out) {
        // dump in format used by gnuplot
        for (int dow = 1; dow <= 7; dow++) {
            for (int hour = 0; hour < 24; hour++) {
                String key = String.format("%d:%02d", dow, hour);
                Integer value = db.get(key);
                if (value == null) {
                    value = 0;
                }
                out.print(String.format("%8d", value));
            }
            out.print("\n");
        }
    }

    public void clear() {
        db.clear();
    }
}
