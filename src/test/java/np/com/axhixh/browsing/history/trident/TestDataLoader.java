
package np.com.axhixh.browsing.history.trident;

import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ashish
 */
public class TestDataLoader {
    public static Map<String, Integer> loadAll() throws IOException {
        return load(new Filter() {

            @Override
            public boolean accept(String... s) {
                return true;
            }
        });
    }

    public static Map<String, Integer> loadFiltered() throws IOException {
        final Map<String, String> placeMap = new HashMap<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                VerifyFilterData.class.getResourceAsStream("/data/moz_places_filtered.csv")),
                ',', '"', 1)) {
            for (String[] line : reader.readAll()) {
                if (isInterestingURL(line[1])) {
                    placeMap.put(line[0], line[1]);
                }
            }
        }

        return load(new Filter() {

            @Override
            public boolean accept(String... s) {
                String place = s[2];
                return placeMap.containsKey(place);
            }
        });
    }

    private static boolean isInterestingURL(String url) {
        return url.toLowerCase().contains("clojure");
    }

    private static Map<String, Integer> load(Filter filter) throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                VerifyData.class.getResourceAsStream("/data/moz_historyvisits.csv")),
                ',', '"', 1)) {
            Map<String, Integer> counter = new HashMap<>();
            for (String[] line : reader.readAll()) {
                if (!filter.accept(line)) {
                    continue;
                }
                String visitDate = line[3];
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(visitDate) / 1000);
                int dow = calendar.get(Calendar.DAY_OF_WEEK);
                int hr = calendar.get(Calendar.HOUR_OF_DAY);
                String key = String.format("%d:%02d", dow, hr);
                Integer count = counter.get(key);
                if (count == null) {
                    count = 0;
                }
                counter.put(key, count + 1);
            }

            return counter;
        }
    }

    private static interface Filter {

        boolean accept(String... s);
    }
}
