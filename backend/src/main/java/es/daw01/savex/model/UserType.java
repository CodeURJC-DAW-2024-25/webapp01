package es.daw01.savex.model;

public enum UserType {
    USER("Usuario"),
    ADMIN("Administrador"),
    ANONYMOUS("Anónimo");

    private String name;

    UserType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

