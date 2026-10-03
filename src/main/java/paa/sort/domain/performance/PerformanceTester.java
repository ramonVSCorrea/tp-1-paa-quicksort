package paa.sort.domain.performance;

import java.util.Arrays;
import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.testdata.DataType;

/** Mede exclusivamente a ordenacao; geracao, validacao e exportacao ficam fora do relogio. */
public final class PerformanceTester {
    private final int warmups;

    public PerformanceTester(int warmups) {
        if (warmups < 0) throw new IllegalArgumentException("Aquecimentos nao podem ser negativos");
        this.warmups = warmups;
    }

    public PerformanceTester() {
        this(3);
    }

    public record RunResult(PerformanceResult performance, int[] sortedArray) { }

    public PerformanceResult testAlgorithmMultipleTimes(SortingAlgorithm algorithm, DataType type,
            int[] data, int iterations) {
        return measure(algorithm, type, data, iterations).performance();
    }

    /** Executa todas as versoes com copias da mesma massa; devolve o primeiro vetor ordenado para exportacao. */
    public RunResult measure(SortingAlgorithm algorithm, DataType type, int[] data, int iterations) {
        if (algorithm == null || type == null || data == null || iterations <= 0) {
            throw new IllegalArgumentException("Algoritmo, tipo, dados e iteracoes devem ser validos");
        }
        int[] expected = data.clone();
        Arrays.sort(expected);

        for (int i = 0; i < warmups; i++) algorithm.sort(data.clone());

        long totalTime = 0, comparisons = 0, movements = 0;
        int[] firstSorted = null;
        for (int i = 0; i < iterations; i++) {
            SortingMetrics metrics = new SortingMetrics();
            int[] input = data.clone();
            long start = System.nanoTime();
            int[] sorted = algorithm.sort(input, metrics);
            totalTime += System.nanoTime() - start;
            if (!Arrays.equals(sorted, expected)) {
                throw new IllegalStateException("Ordenacao incorreta: " + algorithm.getName()
                        + " / " + type + " / " + data.length);
            }
            if (firstSorted == null) firstSorted = sorted;
            comparisons += metrics.getComparisons();
            movements += metrics.getSwaps();
        }
        PerformanceResult result = new PerformanceResult(algorithm.getName(), type.getDescription(), data.length,
                totalTime / iterations, true, comparisons / iterations, movements / iterations);
        return new RunResult(result, firstSorted);
    }
}
