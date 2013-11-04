package np.com.axhixh.browsing.history.trident.operator;

import backtype.storm.tuple.Values;
import java.util.Calendar;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class DateSplitterFunc extends BaseFunction {

    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
        String visitDate = tuple.getString(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(visitDate)/1000);
        collector.emit(new Values(calendar.get(Calendar.DAY_OF_WEEK),
        calendar.get(Calendar.HOUR_OF_DAY)));
    }

}
