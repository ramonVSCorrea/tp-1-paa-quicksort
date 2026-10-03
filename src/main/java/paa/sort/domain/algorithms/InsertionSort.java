package paa.sort.domain.algorithms;

import paa.sort.domain.performance.SortingMetrics;

/**
 * Implementacao do Insertion Sort para uso no Quicksort hibrido
 */
public class InsertionSort {

    /**
     * Ordena um subarray usando Insertion Sort
     * @param array Array a ser ordenado
     * @param low Indice inicial
     * @param high Indice final
     */
    public static void sort(int[] array, int low, int high) {
        sort(array, low, high, new SortingMetrics());
    }

    /**
     * Ordena um subarray usando Insertion Sort com metricas
     * @param array Array a ser ordenado
     * @param low Indice inicial
     * @param high Indice final
     * @param metrics Objeto para coletar metricas
     */
    public static void sort(int[] array, int low, int high, SortingMetrics metrics) {
        for (int currentIndex = low + 1; currentIndex <= high; currentIndex++) {
            int valueToInsert = array[currentIndex];
            int comparisonIndex = currentIndex - 1;

            while (comparisonIndex >= low) {
                metrics.incrementComparisons(); // Comparacao: array[comparisonIndex] > valueToInsert
                if (array[comparisonIndex] > valueToInsert) {
                    array[comparisonIndex + 1] = array[comparisonIndex];
                    metrics.incrementSwaps(); // Deslocamento: contado como movimento, nao troca literal
                    comparisonIndex--;
                } else {
                    break;
                }
            }

            int insertionPosition = comparisonIndex + 1;
            if (insertionPosition != currentIndex) { // So conta como troca se houve movimento
                array[insertionPosition] = valueToInsert;
                metrics.incrementSwaps(); // Insercao do valueToInsert na posicao correta
            }
        }
    }
}
