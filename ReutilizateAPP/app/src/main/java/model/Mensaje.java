package model;

public class Mensaje {

    private int id;
    private int idEmisor;
    private int idReceptor;
    private String contenido;
    private String fecha;

    public Mensaje(int id, int idEmisor, int idReceptor, String contenido, String fecha) {
        this.id = id;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
        this.contenido = contenido;
        this.fecha = fecha;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public int getIdEmisor() {
        return idEmisor;
    }

    public int getIdReceptor() {
        return idReceptor;
    }

    public String getContenido() {
        return contenido;
    }

    public String getFecha() {
        return fecha;
    }

    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setIdEmisor(int idEmisor) {
        this.idEmisor = idEmisor;
    }

    public void setIdReceptor(int idReceptor) {
        this.idReceptor = idReceptor;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
