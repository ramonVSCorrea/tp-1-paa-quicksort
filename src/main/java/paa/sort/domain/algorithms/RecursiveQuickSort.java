package paa.sort.domain.algorithms;

import paa.sort.domain.SortingAlgorithm;
import paa.sort.domain.performance.SortingMetrics;

/**
 * Implementacao do Quicksort recursivo tradicional
 */
public class RecursiveQuickSort implements SortingAlgorithm {

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
            int pivotIndex = partition(array, low, high, metrics);
            quickSort(array, low, pivotIndex - 1, metrics);
            quickSort(array, pivotIndex + 1, high, metrics);
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
        return "Quicksort Recursivo";
    }
}
