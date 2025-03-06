package es.daw01.savex.model;

public enum SupermarketType {
    MERCADONA("Mercadona"),
    DIA("Dia"),
    ELCORTEINGLES("ElCorteIngles"),
    CONSUM("Consum"),
    BM("BM");

    private final String name;

    SupermarketType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SupermarketType fromString(String name) {
        for (SupermarketType type : SupermarketType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for name: " + name);
    }
}
