
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
public class TridentStreamHistoryTest extends TridentTest {

    private static Map<String, Integer> expected;

    @BeforeClass
    public static void setup() throws IOException {
        expected = TestDataLoader.loadAll();
    }

    @Test
    public void testApp() {
        try {
            try (TridentStreamHistory app = new TridentStreamHistory()) {
                Utils.sleep(15000);
                MockDB db = MockDB.getInstance();
                verify(db, expected);
            }
        } catch (IOException ignored) {
            // ignoring it
        }
    }

}
