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
import np.com.axhixh.browsing.history.trident.operator.VisitAggregator;
import np.com.axhixh.browsing.history.trident.source.HistorySpout;
import storm.trident.Stream;
import storm.trident.TridentTopology;

public class TridentStreamHistory implements Closeable {

    private final LocalCluster cluster;

    public TridentStreamHistory() throws IOException {
        cluster = new LocalCluster();
        cluster.submitTopology("heatmap", new Config(), buildTopology());
    }

    private StormTopology buildTopology() throws IOException {
        TridentTopology topology = new TridentTopology();

        List<String[]> lines;
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                TridentStreamHistory.class.getResourceAsStream("/data/moz_historyvisits.csv")),
                ',', '"', 1)) {
            lines = reader.readAll();
        }
        Stream historyStream = topology.newStream("history-source", new HistorySpout(lines));
        historyStream.shuffle();
        historyStream.each(new Fields("visit_date"), new DateSplitterFunc(),
                new Fields("dow", "hour"))
                .partitionBy(new Fields("dow", "hour"))
                .partitionAggregate(new Fields("dow", "hour"), new VisitAggregator(),
                        new Fields("visit_counts"))
                .parallelismHint(3)
                .each(new Fields("visit_counts"), new GlobalAggregator());

        return topology.build();
    }

    @Override
    public void close() throws IOException {
        cluster.shutdown();
    }

    public static void main(String[] args) throws IOException {
        
        try (TridentStreamHistory app = new TridentStreamHistory()) {
            Utils.sleep(15000); // from log 5000 is what it takes on my macbook air
            MockDB.getInstance().dump(System.out);
            try (PrintStream out = new PrintStream(new File("stream-heatmap.txt"))) {
                MockDB.getInstance().dumpForPlot(out);
            }
        }
    }

}
