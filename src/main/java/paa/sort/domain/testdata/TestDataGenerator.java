package paa.sort.domain.testdata;

import java.util.Random;

/** Gera massas reproduziveis quando instanciado com seed fixa. */
public final class TestDataGenerator {
    private final Random random;

    public TestDataGenerator(long seed) {
        random = new Random(seed);
    }

    public int[] generateData(DataType type, int size) {
        if (type == null || size <= 0) throw new IllegalArgumentException("Tipo e tamanho devem ser validos");
        int[] data = new int[size];
        switch (type) {
            case RANDOM -> {
                for (int i = 0; i < size; i++) data[i] = random.nextInt(size * 10);
            }
            case SORTED, WORST_CASE -> {
                for (int i = 0; i < size; i++) data[i] = i;
            }
            case REVERSE_SORTED -> {
                for (int i = 0; i < size; i++) data[i] = size - 1 - i;
            }
            case MANY_DUPLICATES -> {
                int uniqueValues = Math.max(1, size / 10);
                for (int i = 0; i < size; i++) data[i] = random.nextInt(uniqueValues);
            }
        }
        return data;
    }
}
