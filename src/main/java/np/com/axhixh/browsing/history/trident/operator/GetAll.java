
package np.com.axhixh.browsing.history.trident.operator;

import backtype.storm.tuple.Values;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import np.com.axhixh.browsing.history.trident.source.HistorySource;
import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseQueryFunction;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class GetAll extends BaseQueryFunction<HistorySource, List<String>> {

    @Override
    public List<List<String>> batchRetrieve(HistorySource s, List<TridentTuple> list) {
        List<String[]> visitsFor = s.getAllVisits();
        List<String> visitDates = new ArrayList<>(visitsFor.size());
        for (String[] entry : visitsFor) {
            visitDates.add(entry[3]);
        }
        return Arrays.asList(visitDates);
    }

    @Override
    public void execute(TridentTuple tt, List<String> values, TridentCollector tc) {
        for (String v : values) {
            tc.emit(new Values(v));
        }
    }

}
