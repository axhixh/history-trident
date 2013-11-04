
package np.com.axhixh.browsing.history.trident.source;

import au.com.bytecode.opencsv.CSVReader;
import backtype.storm.task.IMetricsContext;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import storm.trident.state.ReadOnlyState;
import storm.trident.state.State;
import storm.trident.state.StateFactory;

/**
 *
 * @author ashish
 */
public class HistorySource extends ReadOnlyState {

    private final List<String[]> lines;

    public HistorySource() throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                HistorySource.class.getResourceAsStream("/data/moz_historyvisits.csv")),
                ',', '"', 1)) {
            lines = reader.readAll();
        }
    }

    public List<String[]> getAllVisits() {
        return lines;
    }

    public static class HistorySourceFactory implements StateFactory {

        @Override
        public State makeState(Map map, IMetricsContext imc, int i, int i1) {
            try {
                return new HistorySource();
            } catch (IOException err) {
                throw new RuntimeException("Unable to load history visit file");
            }
        }
    }
}
