
package np.com.axhixh.browsing.history.trident;

import np.com.axhixh.browsing.history.trident.persistence.MockDB;
import au.com.bytecode.opencsv.CSVReader;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import np.com.axhixh.browsing.history.trident.operator.DateSplitterFunc;
import np.com.axhixh.browsing.history.trident.operator.GlobalAggregator;
import np.com.axhixh.browsing.history.trident.operator.PlaceQuery;
import np.com.axhixh.browsing.history.trident.operator.PlaceUpdater;
import np.com.axhixh.browsing.history.trident.operator.URLFilter;
import np.com.axhixh.browsing.history.trident.operator.VisitAggregator;
import np.com.axhixh.browsing.history.trident.persistence.PlaceDBFactory;
import np.com.axhixh.browsing.history.trident.source.HistorySpout;
import np.com.axhixh.browsing.history.trident.source.PlacesSpout;
import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class TridentStreamFiltered implements Closeable {

    private final LocalCluster cluster;
    private final TridentTopology topology;
    private TridentState placeState;
    
    public TridentStreamFiltered() {
        cluster = new LocalCluster();
        topology = new TridentTopology();
    }
    
    public void processPlaces() throws IOException {
        cluster.submitTopology("places", new Config(), buildPlacesTopology());
    }
    
    public void processHistory() throws IOException {
        cluster.submitTopology("history", new Config(), buildHistoryTopology());
    }

    private StormTopology buildPlacesTopology() throws IOException {
        List<String[]> places = load("/data/moz_places_filtered.csv");
        placeState = topology.newStream("place-source", new PlacesSpout(places))
                .shuffle()
                .each(new Fields("id", "url"), new URLFilter("clojure"))
                .each(new Fields("id", "url"), new PrintFilter()) // just for debug
                .partitionPersist(new PlaceDBFactory(), new Fields("id", "url"), 
                        new PlaceUpdater());
        return topology.build();
    }
    
    private StormTopology buildHistoryTopology() throws IOException {
        List<String[]> history = load("/data/moz_historyvisits.csv");
        topology.newStream("history-source", new HistorySpout(history))
                .shuffle()
                .stateQuery(placeState, new Fields("place_id", "visit_date"), new PlaceQuery(),
                        new Fields("vdate"))
                .each(new Fields("vdate"), new DateSplitterFunc(), new Fields("dow", "hour"))
                .partitionBy(new Fields("dow", "hour"))
                .partitionAggregate(new Fields("dow", "hour"), new VisitAggregator(), new Fields("visit_counts"))
                .parallelismHint(3)
                .each(new Fields("visit_counts"), new GlobalAggregator());

        return topology.build();
    }

    private static List<String[]> load(String resourceName) throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                TridentStreamHistory.class.getResourceAsStream(resourceName)),
                ',', '"', 1)) {
            return reader.readAll();
        }
    }

    @Override
    public void close() throws IOException {
        cluster.shutdown();
    }

    public static void main(String[] args) throws IOException {
        try (TridentStreamFiltered app = new TridentStreamFiltered()) {
            app.processPlaces();
            Utils.sleep(5000); 
            app.processHistory();
            Utils.sleep(10000);
            MockDB.getInstance().dump(System.out);
            try (PrintStream out = new PrintStream(new File("stream-filtered-heatmap.txt"))) {
                MockDB.getInstance().dumpForPlot(out);
            }
        }
    }
    
    static class PrintFilter extends BaseFilter {

        @Override
        public boolean isKeep(TridentTuple tuple) {
            System.out.println(tuple);
            return true;
        }
        
    }
}
