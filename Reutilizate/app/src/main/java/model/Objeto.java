package model;

public class Objeto {

    private int id;
    private String nombre;
    private String descripcion;
    private Usuario propietario;
    private String imagen;


    public Objeto(int id, String nombre, String descripcion, Usuario propietario, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.propietario = propietario;
        this.imagen = imagen;
    }


    // GETTERS
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public String getImagen() {
        return imagen;
    }


    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
