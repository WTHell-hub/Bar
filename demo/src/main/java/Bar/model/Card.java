package Bar.model;

public class Card {
    private int id;
    private String nombre;
    private String fecha;
    private double total;

    public Card(int id, String nombre, String fecha, double total) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
