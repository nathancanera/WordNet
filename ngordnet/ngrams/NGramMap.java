package ngordnet.ngrams;
import edu.princeton.cs.algs4.In;
import java.util.Collection;
import java.util.HashMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    private HashMap<String, TimeSeries> words;
    private TimeSeries counts;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In file = new In(wordsFilename);
        In cFile = new In(countsFilename);
        words = new HashMap<>();
        counts = new TimeSeries();
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] arrOfStr = line.split("\\s+");
            if (words.get(arrOfStr[0]) != null) {
                words.get(arrOfStr[0]).put(Integer.parseInt(arrOfStr[1]), Double.parseDouble(arrOfStr[2]));
            } else {
                TimeSeries occurrence = new TimeSeries();
                occurrence.put(Integer.parseInt(arrOfStr[1]), Double.parseDouble(arrOfStr[2]));
                words.put(arrOfStr[0], occurrence);
            }
        }
        while (cFile.hasNextLine()) {
            String line = cFile.readLine();
            String[] arrOfStr = line.split(",");
            counts.put(Integer.parseInt(arrOfStr[0]), Double.parseDouble(arrOfStr[1]));
        }
    }

    public static void main(String[] args) {

    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        return new TimeSeries(words.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        return new TimeSeries(words.get(word), MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(counts, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!words.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(words.get(word).dividedBy(counts), startYear, endYear);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (!words.containsKey(word)) {
            return new TimeSeries();
        }
        return words.get(word).dividedBy(counts);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words1,
                                          int startYear, int endYear) {
        TimeSeries sum = new TimeSeries();
        for (String word : words1) {
            sum = sum.plus(weightHistory(word));
        }
        return new TimeSeries(sum, startYear, endYear);
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words1) {
        TimeSeries sum = new TimeSeries();
        for (String word : words1) {
            sum = sum.plus(weightHistory(word));
        }
        return sum;
    }
}
