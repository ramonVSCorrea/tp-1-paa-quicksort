package paa.sort.domain.performance;

import paa.sort.domain.algorithms.HybridQuickSort;
import paa.sort.domain.testdata.DataType;
import paa.sort.domain.testdata.TestDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsavel por determinar empiricamente o melhor threshold (M) para o
 * Quicksort hibrido
 */
public class ThresholdOptimizer {
    private final PerformanceTester performanceTester;

    public ThresholdOptimizer() {
        this.performanceTester = new PerformanceTester(5);
    }

    /**
     * Determina o melhor threshold testando diferentes valores
     */
    public OptimizationResult findOptimalThreshold(int arraySize, int iterations) {
        if (iterations <= 0) throw new IllegalArgumentException("Iteracoes devem ser positivas");
        List<ThresholdResult> results = new ArrayList<>();

        // Testa diferentes valores de threshold
        int[] thresholds = { 5, 10, 15, 20, 25, 30, 40, 50, 75, 100 };
        int[] calibrationData = new TestDataGenerator(42).generateData(DataType.RANDOM, arraySize);

        for (int threshold : thresholds) {
            HybridQuickSort algorithm = new HybridQuickSort(threshold);

            // Testa com dados aleatorios
            PerformanceResult result = performanceTester.testAlgorithmMultipleTimes(
                    algorithm, DataType.RANDOM, calibrationData, iterations);

            results.add(new ThresholdResult(threshold, result.getExecutionTimeNanos()));

        }

        // Encontra o melhor threshold
        ThresholdResult best = results.stream()
                .min((r1, r2) -> Long.compare(r1.getExecutionTime(), r2.getExecutionTime()))
                .orElse(results.get(0));

        return new OptimizationResult(best.getThreshold(), results);
    }

    /**
     * Classe interna para armazenar resultado de um threshold especifico
     */
    public static class ThresholdResult {
        private final int threshold;
        private final long executionTime;

        public ThresholdResult(int threshold, long executionTime) {
            this.threshold = threshold;
            this.executionTime = executionTime;
        }

        public int getThreshold() {
            return threshold;
        }

        public long getExecutionTime() {
            return executionTime;
        }
    }

    /**
     * Classe para o resultado da otimizacao
     */
    public static class OptimizationResult {
        private final int optimalThreshold;
        private final List<ThresholdResult> allResults;

        public OptimizationResult(int optimalThreshold, List<ThresholdResult> allResults) {
            this.optimalThreshold = optimalThreshold;
            this.allResults = allResults;
        }

        public int getOptimalThreshold() {
            return optimalThreshold;
        }

        public List<ThresholdResult> getAllResults() {
            return allResults;
        }
    }
}
