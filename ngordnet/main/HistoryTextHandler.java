package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap map;

    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        String response = "";
        for (String word : words) {
            response += word + ":" + " " + this.map.weightHistory(word, q.startYear(), q.endYear()).toString() + "\n";
        }
        return response;
    }
}
