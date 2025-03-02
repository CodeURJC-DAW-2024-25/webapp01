package es.daw01.savex.model;

public enum SupermarketType {
    MERCADONA("Mercadona"),
    CARREFOUR("Carrefour"),
    DIA("Dia"),
    ELCORTEINGLES("El Corte Inglés"),
    CONSUM("Consum"),
    BM("BM");

    private final String name;

    SupermarketType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
