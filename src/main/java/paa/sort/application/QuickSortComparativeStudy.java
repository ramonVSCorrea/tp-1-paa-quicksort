package paa.sort.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.algorithms.HybridQuickSort;
import paa.sort.domain.algorithms.ImprovedHybridQuickSort;
import paa.sort.domain.algorithms.RecursiveQuickSort;
import paa.sort.domain.performance.PerformanceResult;
import paa.sort.domain.performance.PerformanceTester;
import paa.sort.domain.performance.ThresholdOptimizer;
import paa.sort.domain.testdata.DataType;
import paa.sort.domain.testdata.TestDataGenerator;
import paa.sort.infrastructure.export.ArrayExporter;

/** Orquestra os experimentos; os algoritmos, a medicao e a exportacao sao independentes. */
public final class QuickSortComparativeStudy {
    private static final int REPETITIONS = 5;
    private static final int[] STANDARD_SIZES = { 100, 500, 1000, 2000, 5000, 10000 };
    private static final int[] LARGE_SIZES = { 100, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000 };
    private static final Locale OUTPUT_LOCALE = Locale.forLanguageTag("pt-BR");
    private static final String TABLE_BORDER = "---------+--------------+--------------+--------------+------------";
    private static final String[] SHORT_NAMES = { "Recursivo", "Hibrido", "Mediana-3" };

    private final PerformanceTester tester = new PerformanceTester(3);
    private final TestDataGenerator generator = new TestDataGenerator(42);
    private final ArrayExporter exporter = new ArrayExporter();

    public void executeCompleteStudy() throws Exception {
        System.out.println("================ ESTUDO COMPARATIVO DE QUICKSORT ================");
        ThresholdOptimizer.OptimizationResult calibration = new ThresholdOptimizer().findOptimalThreshold(1000, 10);
        exporter.saveThresholdResults(calibration);
        int m = calibration.getOptimalThreshold();
        System.out.printf("M calibrado: %d | 3 aquecimentos + %d medicoes por teste%n", m, REPETITIONS);
        System.out.println("Tempos medios em ms | Recursivo: pivo final | Hibrido: pivo final");
        System.out.println("Mediana-3: hibrido com mediana-de-tres | M igual para os dois hibridos");

        List<SortingAlgorithm> algorithms = List.of(new RecursiveQuickSort(),
                new HybridQuickSort(m), new ImprovedHybridQuickSort(m));
        List<PerformanceResult> allResults = new ArrayList<>();

        for (DataType type : DataType.values()) {
            printTableHeader(type.getDescription());
            List<PerformanceResult> typeResults = new ArrayList<>();
            // Com pivo final, dados ordenados/inversos podem provocar recursao linear e custo O(n²).
            int[] sizes = (type == DataType.RANDOM || type == DataType.MANY_DUPLICATES)
                    ? LARGE_SIZES : STANDARD_SIZES;
            for (int size : sizes) {
                int[] data = generator.generateData(type, size);
                exporter.saveOriginalArray(type, size, data);
                List<PerformanceResult> results = compare(algorithms, type, data);
                typeResults.addAll(results);
                allResults.addAll(results);
                printTableRow(size, results);
            }
            exporter.saveTestResults(typeResults, type.getDescription(), type);
        }

        // Os demais tamanhos do pior caso ja foram incluidos no loop acima.
        int[] worstCase = generator.generateData(DataType.WORST_CASE, 200);
        exporter.saveOriginalArray(DataType.WORST_CASE, 200, worstCase);
        List<PerformanceResult> extra = compare(algorithms, DataType.WORST_CASE, worstCase);
        allResults.addAll(extra);
        exporter.saveTestResults(extra, "Analise_Pior_Caso", DataType.WORST_CASE);
        printTableHeader("Pior caso explicito (tamanho adicional)");
        printTableRow(200, extra);

        exporter.saveGeneralSummary(allResults, m);
        exporter.saveCsvResults(allResults);
        System.out.println("\nComparacoes e trocas/movimentos: arrays_testados/resultados.csv");
        System.out.println("Calibracao de M: arrays_testados/calibracao_m.csv");
        System.out.println("Estudo concluido. Demais arquivos em arrays_testados/.");
    }

    private List<PerformanceResult> compare(List<SortingAlgorithm> algorithms, DataType type, int[] data) {
        List<PerformanceResult> results = new ArrayList<>();
        for (SortingAlgorithm algorithm : algorithms) {
            PerformanceTester.RunResult run;
            try {
                run = tester.measure(algorithm, type, data, REPETITIONS);
            } catch (StackOverflowError e) {
                throw new IllegalStateException("Pilha esgotada: " + algorithm.getName()
                        + " / " + type + " / " + data.length, e);
            }
            exporter.saveSortedArray(algorithm.getName(), type, data.length, run.sortedArray());
            PerformanceResult result = run.performance();
            results.add(result);
        }
        return results;
    }

    private void printTableHeader(String title) {
        System.out.println("\n" + title.toUpperCase(OUTPUT_LOCALE));
        System.out.println(TABLE_BORDER);
        System.out.printf("%8s | %12s | %12s | %12s | %s%n",
                "Tamanho", "Recursivo", "Hibrido", "Mediana-3", "Menor tempo");
        System.out.println(TABLE_BORDER);
    }

    private void printTableRow(int size, List<PerformanceResult> results) {
        PerformanceResult fastest = results.stream()
                .min(Comparator.comparingLong(PerformanceResult::getExecutionTimeNanos)).orElseThrow();
        System.out.printf(OUTPUT_LOCALE, "%,8d | %12.3f | %12.3f | %12.3f | %s%n",
                size, results.get(0).getExecutionTimeMillis(), results.get(1).getExecutionTimeMillis(),
                results.get(2).getExecutionTimeMillis(), SHORT_NAMES[results.indexOf(fastest)]);
    }
}
