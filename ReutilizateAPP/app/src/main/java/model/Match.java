package model;

public class Match {

    private String nombreObjeto;
    private String imagenObjeto;
    private String nombreUsuario;
    private int idUsuarioOtro;


    public Match(String nombreObjeto, String imagenObjeto,
                 String nombreUsuario, int idUsuarioOtro) {

        this.nombreObjeto = nombreObjeto;
        this.imagenObjeto = imagenObjeto;
        this.nombreUsuario = nombreUsuario;
        this.idUsuarioOtro = idUsuarioOtro;
    }

    // Getters
    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public String getImagenObjeto() {
        return imagenObjeto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public int getIdUsuarioOtro() {
        return idUsuarioOtro;
    }

    // Setters
    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public void setImagenObjeto(String imagenObjeto) {
        this.imagenObjeto = imagenObjeto;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setIdUsuarioOtro(int idUsuarioOtro) {
        this.idUsuarioOtro = idUsuarioOtro;
    }
}
