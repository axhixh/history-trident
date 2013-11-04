package np.com.axhixh.browsing.history.trident.operator;

import java.util.ArrayList;
import java.util.List;
import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

/**
 *
 * @author ashish
 */
public class URLFilter extends BaseFilter {

    private final List<String> wordList;

    public URLFilter(String... wordList) {
        this.wordList = new ArrayList<>(wordList.length);
        for (String word : wordList) {
            this.wordList.add(word.toLowerCase());
        }
    }

    @Override
    public boolean isKeep(TridentTuple tt) {
        String url = tt.getStringByField("url");
        for (String s : wordList) {
            if (url.toLowerCase().contains(s)) {
                return true;
            }
        }
        return false;
    }

}
