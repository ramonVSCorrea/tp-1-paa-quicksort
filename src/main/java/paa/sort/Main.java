package paa.sort;

import paa.sort.application.QuickSortComparativeStudy;

/** Ponto de entrada do estudo. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        if (args.length != 0) {
            System.err.println("Uso: java -cp target/classes paa.sort.Main");
            System.exit(1);
        }
        try {
            new QuickSortComparativeStudy().executeCompleteStudy();
        } catch (Exception e) {
            System.err.println("Falha no estudo: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
