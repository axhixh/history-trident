package np.com.axhixh.browsing.history.trident;

import np.com.axhixh.browsing.history.trident.persistence.MockDB;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import np.com.axhixh.browsing.history.trident.operator.DateSplitterFunc;
import np.com.axhixh.browsing.history.trident.operator.GetAll;
import np.com.axhixh.browsing.history.trident.operator.GlobalAggregator;
import np.com.axhixh.browsing.history.trident.operator.VisitAggregator;
import np.com.axhixh.browsing.history.trident.source.HistorySource;
import storm.trident.Stream;
import storm.trident.TridentState;
import storm.trident.TridentTopology;

/**
 * Counting visits per hour per day of week using DRPC.
 *
 * @author ashish
 */
public class TridentDRPCHistory implements Closeable {

    private final LocalCluster cluster;
    private final LocalDRPC drpc;

    public TridentDRPCHistory() {
        drpc = new LocalDRPC();
        cluster = new LocalCluster();
        cluster.submitTopology("heatmap-topology", new Config(), buildTopology());
    }

    private StormTopology buildTopology() {
        TridentTopology topology = new TridentTopology();
        TridentState visitState = topology.newStaticState(new HistorySource.HistorySourceFactory());

        Stream historyStream = topology.newDRPCStream("heatmap", drpc);
        Stream stateQuery = historyStream.stateQuery(visitState, new GetAll(), new Fields("visit_date"));
        stateQuery.each(new Fields("visit_date"), new DateSplitterFunc(),
                new Fields("dow", "hour"))
                .partitionBy(new Fields("dow", "hour"))
                .partitionAggregate(new Fields("dow", "hour"), new VisitAggregator(), new Fields("visit_counts"))
                .parallelismHint(3)
                .each(new Fields("visit_counts"), new GlobalAggregator());

        return topology.build();
    }

    public String execute() {
        return drpc.execute("heatmap", null);
    }

    @Override
    public void close() throws IOException {
        cluster.shutdown();
    }

    public static void main(String[] args) throws IOException {
        try (TridentDRPCHistory app = new TridentDRPCHistory()) {
            System.out.println("Result: " + app.execute());
            MockDB.getInstance().dump(System.out);
            try (PrintStream out = new PrintStream(new File("drpc-heatmap.txt"))) {
                MockDB.getInstance().dumpForPlot(out);
            }
        }
    }
}
