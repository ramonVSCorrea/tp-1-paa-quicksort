package paa.sort.domain;

import paa.sort.domain.performance.SortingMetrics;

/**
 * Interface para algoritmos de ordenacao
 */
public interface SortingAlgorithm {
    /**
     * Ordena um array de inteiros
     * @param array Array a ser ordenado
     * @return Array ordenado
     */
    int[] sort(int[] array);

    /**
     * Ordena um array de inteiros coletando metricas de performance
     * @param array Array a ser ordenado
     * @param metrics Objeto para coletar metricas de comparacoes e trocas
     * @return Array ordenado
     */
    int[] sort(int[] array, SortingMetrics metrics);

    /**
     * Retorna o nome do algoritmo
     * @return Nome do algoritmo
     */
    String getName();
}
