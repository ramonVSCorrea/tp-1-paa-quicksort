package paa.sort.domain.algorithms;

import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.performance.SortingMetrics;

/**
 * Implementacao do Quicksort hibrido que usa Insertion Sort para subarrays pequenos
 */
public class HybridQuickSort implements SortingAlgorithm {
    private final int threshold;

    public HybridQuickSort(int threshold) {
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
                int pivotIndex = partition(array, low, high, metrics);
                quickSort(array, low, pivotIndex - 1, metrics);
                quickSort(array, pivotIndex + 1, high, metrics);
            }
        }
    }

    private int partition(int[] array, int low, int high, SortingMetrics metrics) {
        int pivotValue = array[high]; // Ultimo elemento como pivo
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
        return "Quicksort Hibrido (M=" + threshold + ")";
    }
}
