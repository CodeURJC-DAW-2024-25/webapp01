package es.daw01.savex.DTOs.lists;

public class CreateListRequest {
    private String listName;
    private String listDescription;

    // Getters and setters
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListDescription() {
        return listDescription;
    }

    public void setListDescription(String listDescription) {
        this.listDescription = listDescription;
    }
}
