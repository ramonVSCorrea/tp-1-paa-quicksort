package paa.sort.infrastructure.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import paa.sort.domain.performance.PerformanceResult;
import paa.sort.domain.performance.ThresholdOptimizer;
import paa.sort.domain.testdata.DataType;

/** Apenas formata e grava os dados; nenhuma escrita ocorre durante a cronometragem. */
public final class ArrayExporter {
    private static final Path ROOT = Path.of("arrays_testados");

    public void saveOriginalArray(DataType type, int size, int[] array) {
        if (type == null || array == null || size != array.length) {
            throw new IllegalArgumentException("Tipo e tamanho devem corresponder ao vetor original");
        }
        Path file = directory(type).resolve("arrays_originais")
                .resolve("array_original_" + type.name().toLowerCase() + "_" + size + ".txt");
        write(file, writer -> writeArray(writer, "ARRAY ORIGINAL TESTADO", type, null, array));
    }

    public void saveSortedArray(String algorithm, DataType type, int size, int[] array) {
        if (algorithm == null || type == null || array == null || size != array.length) {
            throw new IllegalArgumentException("Dados do vetor ordenado invalidos");
        }
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) throw new IllegalArgumentException("Vetor nao esta ordenado");
        }
        String filename = "array_ordenado_" + algorithm.replace(" ", "_").replace("(", "")
                .replace(")", "").replace("=", "-").replace(",", "")
                + "_" + type.name().toLowerCase() + "_" + size + ".txt";
        Path file = directory(type).resolve("arrays_ordenados").resolve(filename);
        write(file, writer -> writeArray(writer, "ARRAY APOS ORDENACAO", type, algorithm, array));
    }

    public void saveTestResults(List<PerformanceResult> results, String label, DataType type) {
        Path file = directory(type).resolve("resultados")
                .resolve("resultados_" + label.toLowerCase().replace(" ", "_") + ".txt");
        write(file, writer -> {
            line(writer, "RESULTADOS DOS TESTES: " + label);
            for (PerformanceResult result : results) {
                line(writer, result.toString());
            }
        });
    }

    public void saveGeneralSummary(List<PerformanceResult> results, int m) {
        write(ROOT.resolve("resumo_geral.txt"), writer -> {
            line(writer, "ESTUDO COMPARATIVO DE QUICKSORT");
            line(writer, "M selecionado: " + m + " | Total de testes: " + results.size());
            line(writer, "Tempos medios em ms; comparacoes entre valores; trocas/movimentos de dados.");
            for (PerformanceResult result : results) line(writer, result.toString());
        });
    }

    public void saveCsvResults(List<PerformanceResult> results) {
        write(ROOT.resolve("resultados.csv"), writer -> {
            line(writer, "tipo;tamanho;algoritmo;tempo_medio_ns;comparacoes_medias;trocas_movimentos_medios;sucesso");
            for (PerformanceResult result : results) {
                line(writer, String.format("%s;%d;%s;%d;%d;%d;%s", result.getDataType(),
                        result.getArraySize(), result.getAlgorithmName(), result.getExecutionTimeNanos(),
                        result.getComparisons(), result.getSwaps(), result.isSuccessful()));
            }
        });
    }

    public void saveThresholdResults(ThresholdOptimizer.OptimizationResult optimization) {
        write(ROOT.resolve("calibracao_m.csv"), writer -> {
            line(writer, "M;tempo_medio_ns;selecionado");
            for (ThresholdOptimizer.ThresholdResult result : optimization.getAllResults()) {
                line(writer, String.format("%d;%d;%s", result.getThreshold(), result.getExecutionTime(),
                        result.getThreshold() == optimization.getOptimalThreshold()));
            }
        });
    }

    private Path directory(DataType type) {
        return ROOT.resolve(switch (type) {
            case RANDOM -> "aleatorio";
            case SORTED -> "ordenado";
            case REVERSE_SORTED -> "ordenado_inverso";
            case MANY_DUPLICATES -> "muitos_duplicados";
            case WORST_CASE -> "pior_caso";
        });
    }

    private void writeArray(BufferedWriter writer, String title, DataType type,
            String algorithm, int[] array) {
        line(writer, title);
        line(writer, "Tipo: " + type.getDescription() + " | Tamanho: " + array.length);
        if (algorithm != null) line(writer, "Algoritmo: " + algorithm);
        for (int i = 0; i < array.length; i++) line(writer, "[" + i + "] = " + array[i]);
        if (algorithm != null) line(writer, "Verificacao: ORDENADO CORRETAMENTE");
    }

    private void write(Path file, Consumer<BufferedWriter> contents) {
        try {
            Files.createDirectories(file.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                contents.accept(writer);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel salvar " + file, e);
        }
    }

    private void line(BufferedWriter writer, String text) {
        try {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao gravar resultado", e);
        }
    }
}
