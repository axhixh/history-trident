package np.com.axhixh.browsing.history.trident.source;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ashish
 */
public class PlacesSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private final List<String[]> lines;
    
    public PlacesSpout(List<String[]> lines) {
        this.lines = lines;
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("id", "url", "title", "rev_host", "visit_count",
                "hidden", "typed", "favicon_id", "frequency", "last_visit_date", "guid"));
    }

    @Override
    public void open(Map map, TopologyContext tc, SpoutOutputCollector soc) {
        this.collector = soc;
    }

    @Override
    public void nextTuple() {
        if (!lines.isEmpty()) {
            collector.emit(new Values(lines.remove(0)));
        }
    }
    
}
