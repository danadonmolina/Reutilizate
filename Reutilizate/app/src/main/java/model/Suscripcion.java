package model;

public class Suscripcion {

    private int id;
    private Usuario usuario;
    private String tipo;
    private boolean activa;



    public Suscripcion(int id, Usuario usuario, String tipo, boolean activa) {
        this.id = id;
        this.usuario = usuario;
        this.tipo = tipo;
        this.activa = activa;
    }


    //   GETTERS

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isActiva() {
        return activa;
    }

    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
