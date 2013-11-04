
package np.com.axhixh.browsing.history.trident;

import java.util.Map;
import np.com.axhixh.browsing.history.trident.persistence.MockDB;
import org.junit.Assert;

/**
 *
 * @author ashish
 */
public class TridentTest {

    public void verify(MockDB db, Map<String, Integer> expected) {

        for (int dow = 1; dow <= 7; dow++) {
            for (int hour = 0; hour < 24; hour++) {
                String key = String.format("%d:%02d", dow, hour);
                Integer expectedValue = expected.get(key);
                Integer actualValue = db.get(key);
                Assert.assertEquals(String.format("%d,%d do not have equal values", dow, hour),
                        expectedValue, actualValue);
            }
        }
    }
}
