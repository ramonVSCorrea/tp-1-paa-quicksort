package paa.sort.domain.performance;

/**
 * Comparacoes entre valores; swaps contam trocas reais no Quicksort e
 * movimentos de escrita no Insertion Sort (inclusive a insercao final).
 */
public class SortingMetrics {
    private long comparisons;
    private long swaps;

    public SortingMetrics() {
        this.comparisons = 0;
        this.swaps = 0;
    }

    public void incrementComparisons() {
        this.comparisons++;
    }

    public void incrementSwaps() {
        this.swaps++;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    @Override
    public String toString() {
        return String.format("Comparacoes: %d, Trocas: %d", comparisons, swaps);
    }
}
