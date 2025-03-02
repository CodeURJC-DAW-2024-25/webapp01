package es.daw01.savex.DTOs;

public class PriceDTO {
    private String total;
    private String per_reference_unit;
    private double reference_units;
    private String reference_unit_name;

    // Getters and Setters ---------------------------------------------------->>
    public String getTotal() {
        return total;  
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public String getPer_reference_unit() {
        return per_reference_unit;
    }
    public void setPer_reference_unit(String per_reference_unit) {
        this.per_reference_unit = per_reference_unit;
    }
    public double getReference_units() {
        return reference_units;
    }
    public void setReference_units(double reference_units) {
        this.reference_units = reference_units;
    }
    public String getReference_unit_name() {
        return reference_unit_name;
    }
    public void setReference_unit_name(String reference_unit_name) {
        this.reference_unit_name = reference_unit_name;
    }
}
