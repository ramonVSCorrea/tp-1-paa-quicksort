package paa.sort;

import java.util.Arrays;
import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.algorithms.*;
import paa.sort.domain.performance.*;
import paa.sort.domain.testdata.*;

/** Testes sem bibliotecas externas: java -cp target/classes paa.sort.SortRegressionTest */
public final class SortRegressionTest {
    public static void main(String[] args) throws Exception {
        int[][] cases = { {}, { 1 }, { 3, 2 }, { 1, 1, 1, 1 },
                { -5, 0, 9, -5, 3, 0 }, { 4, 3, 2, 1, 0 }, { 0, 1, 2, 3, 4 } };
        for (int threshold : new int[] { 2, 3, 5, 10, 50 }) {
            SortingAlgorithm[] algorithms = { new RecursiveQuickSort(),
                    new HybridQuickSort(threshold), new ImprovedHybridQuickSort(threshold) };
            for (SortingAlgorithm algorithm : algorithms) {
                for (int[] data : cases) check(algorithm, data);
                for (DataType type : DataType.values()) {
                    for (int size : new int[] { 2, 3, 10, 100, 1000 }) {
                        check(algorithm, new TestDataGenerator(42).generateData(type, size));
                    }
                }
            }
        }
        for (int a = 0; a < 3; a++) for (int b = 0; b < 3; b++)
            for (int c = 0; c < 3; c++) for (int d = 0; d < 3; d++) {
                int[] input = { a, b, c, d };
                check(new HybridQuickSort(3), input);
                check(new ImprovedHybridQuickSort(3), input);
            }
        int[] ordered = new TestDataGenerator(42).generateData(DataType.WORST_CASE, 100);
        SortingMetrics metrics = new SortingMetrics();
        new RecursiveQuickSort().sort(ordered, metrics);
        if (metrics.getComparisons() != 100L * 99 / 2) throw new AssertionError("Pior caso nao forcado");
        PerformanceResult result = new PerformanceTester().testAlgorithmMultipleTimes(
                new ImprovedHybridQuickSort(10), DataType.RANDOM, new int[] { 3, 2, 1 }, 3);
        if (!result.isSuccessful() || result.getComparisons() <= 0) throw new AssertionError("Medicao invalida");
        int[] repeated = { 4, 1, 4, 2 };
        PerformanceTester.RunResult measured = new PerformanceTester(2).measure(
                new HybridQuickSort(3), DataType.MANY_DUPLICATES, repeated, 5);
        if (!Arrays.equals(repeated, new int[] { 4, 1, 4, 2 })
                || !Arrays.equals(measured.sortedArray(), new int[] { 1, 2, 4, 4 })) {
            throw new AssertionError("Medicao alterou a massa original ou nao retornou o resultado correto");
        }
        SortingAlgorithm broken = new SortingAlgorithm() {
            public int[] sort(int[] values) { return values; }
            public int[] sort(int[] values, SortingMetrics counters) { return values; }
            public String getName() { return "Incorreto"; }
        };
        try {
            new PerformanceTester(0).measure(broken, DataType.RANDOM, repeated, 2);
            throw new AssertionError("Algoritmo incorreto deveria ser rejeitado");
        } catch (IllegalStateException expected) {
            // O teste so passa se a validacao detectar o resultado incorreto.
        }
        System.out.println("Testes de ordenacao e metricas: OK");
    }

    private static void check(SortingAlgorithm algorithm, int[] input) {
        int[] original = input.clone();
        int[] expected = input.clone();
        Arrays.sort(expected);
        SortingMetrics metrics = new SortingMetrics();
        int[] actual = algorithm.sort(input, metrics);
        if (!Arrays.equals(expected, actual) || !Arrays.equals(original, input)) {
            throw new AssertionError(algorithm.getName() + " falhou em " + Arrays.toString(original));
        }
    }
}
