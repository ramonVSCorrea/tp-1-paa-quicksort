package paa.sort.domain.performance;

/**
 * Resultado de um teste de performance
 */
public class PerformanceResult {
    private static final double NANOS_TO_MILLIS = 1_000_000.0;

    private final String algorithmName;
    private final String dataType;
    private final int arraySize;
    private final long executionTimeNanos;
    private final boolean successful;
    private final long comparisons;
    private final long swaps;

    public PerformanceResult(String algorithmName, String dataType, int arraySize,
                           long executionTimeNanos, boolean successful, long comparisons, long swaps) {
        this.algorithmName = algorithmName;
        this.dataType = dataType;
        this.arraySize = arraySize;
        this.executionTimeNanos = executionTimeNanos;
        this.successful = successful;
        this.comparisons = comparisons;
        this.swaps = swaps;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getDataType() {
        return dataType;
    }

    public int getArraySize() {
        return arraySize;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public double getExecutionTimeMillis() {
        return executionTimeNanos / NANOS_TO_MILLIS;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Tamanho: %d | Tempo: %.2f ms | Comp: %d | Trocas: %d | Sucesso: %s",
                algorithmName, dataType, arraySize, getExecutionTimeMillis(), comparisons, swaps, successful);
    }
}
