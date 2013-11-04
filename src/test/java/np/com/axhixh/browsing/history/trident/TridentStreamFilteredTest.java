
package np.com.axhixh.browsing.history.trident;

import backtype.storm.utils.Utils;
import java.io.IOException;
import java.util.Map;
import np.com.axhixh.browsing.history.trident.persistence.MockDB;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ashish
 */
public class TridentStreamFilteredTest extends TridentTest {

    private static Map<String, Integer> expected;

    @BeforeClass
    public static void setup() throws IOException {
        expected = TestDataLoader.loadFiltered();
    }

    @Test
    public void testApp() {
        try {
            try (TridentStreamFiltered app = new TridentStreamFiltered()) {
                app.processPlaces();
                Utils.sleep(5000); 
                app.processHistory();
                Utils.sleep(10000);
                MockDB db = MockDB.getInstance();
                verify(db, expected);
            }
        } catch (IOException ignored) {
            // ignoring it
        }
    }

}
