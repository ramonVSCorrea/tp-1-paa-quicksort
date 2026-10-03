package paa.sort.domain.testdata;

/**
 * Enum para diferentes tipos de arranjos de dados de teste
 */
public enum DataType {
    RANDOM("Aleatorio"),
    SORTED("Ordenado"),
    REVERSE_SORTED("Ordenado Inverso"),
    MANY_DUPLICATES("Muitos Duplicados"),
    WORST_CASE("Pior Caso");

    private final String description;

    DataType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
