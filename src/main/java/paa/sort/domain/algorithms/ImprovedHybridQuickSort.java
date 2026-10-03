package paa.sort.domain.algorithms;

import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.performance.SortingMetrics;

/**
 * Implementacao do Quicksort hibrido melhorado com mediana-de-tres para escolha do pivo
 */
public class ImprovedHybridQuickSort implements SortingAlgorithm {
    private final int threshold;

    public ImprovedHybridQuickSort(int threshold) {
        if (threshold < 2) throw new IllegalArgumentException("M deve ser pelo menos 2");
        this.threshold = threshold;
    }

    @Override
    public int[] sort(int[] array) {
        return sort(array, new SortingMetrics());
    }

    @Override
    public int[] sort(int[] array, SortingMetrics metrics) {
        if (array == null || array.length <= 1) {
            return array;
        }
        int[] sortedArray = array.clone();
        quickSort(sortedArray, 0, sortedArray.length - 1, metrics);
        return sortedArray;
    }

    private void quickSort(int[] array, int low, int high, SortingMetrics metrics) {
        if (low < high) {
            // Se o subarray e pequeno, usa Insertion Sort
            int subarraySize = high - low + 1;
            if (subarraySize < threshold) {
                InsertionSort.sort(array, low, high, metrics);
            } else {
                // Usa mediana-de-tres para escolher o pivo
                int medianIndex = medianOfThree(array, low, high, metrics);
                swap(array, medianIndex, high, metrics); // Move o pivo para o final

                int partitionIndex = partition(array, low, high, metrics);
                quickSort(array, low, partitionIndex - 1, metrics);
                quickSort(array, partitionIndex + 1, high, metrics);
            }
        }
    }

    /**
     * Encontra a mediana de tres elementos (primeiro, meio e ultimo)
     * e retorna o indice do elemento mediano
     */
    private int medianOfThree(int[] array, int low, int high, SortingMetrics metrics) {
        int midIndex = low + (high - low) / 2;

        metrics.incrementComparisons(); // Comparacao: array[low] > array[midIndex]
        if (array[low] > array[midIndex]) {
            swap(array, low, midIndex, metrics);
        }

        metrics.incrementComparisons(); // Comparacao: array[midIndex] > array[high]
        if (array[midIndex] > array[high]) {
            swap(array, midIndex, high, metrics);
        }

        metrics.incrementComparisons(); // Comparacao: array[low] > array[midIndex]
        if (array[low] > array[midIndex]) {
            swap(array, low, midIndex, metrics);
        }

        return midIndex; // O elemento do meio e a mediana
    }

    private int partition(int[] array, int low, int high, SortingMetrics metrics) {
        int pivotValue = array[high];
        int partitionIndex = low - 1;

        for (int currentIndex = low; currentIndex < high; currentIndex++) {
            metrics.incrementComparisons(); // Comparacao: array[currentIndex] <= pivotValue
            if (array[currentIndex] <= pivotValue) {
                partitionIndex++;
                swap(array, partitionIndex, currentIndex, metrics);
            }
        }
        swap(array, partitionIndex + 1, high, metrics);
        return partitionIndex + 1;
    }

    private void swap(int[] array, int firstIndex, int secondIndex, SortingMetrics metrics) {
        if (firstIndex != secondIndex) { // So conta como troca se as posicoes forem diferentes
            metrics.incrementSwaps();
            int tempValue = array[firstIndex];
            array[firstIndex] = array[secondIndex];
            array[secondIndex] = tempValue;
        }
    }

    @Override
    public String getName() {
        return "Quicksort Hibrido Melhorado (M=" + threshold + ", Mediana-de-3)";
    }
}
