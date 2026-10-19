package modelo;

public class Practica {
    private int idPractica;
    private String codigoNomenclador;
    private String nombre;
    private String descripcion;
    private int idEspecialidad;

    public Practica() {}

    public Practica(int idPractica, String codigoNomenclador, String nombre, String descripcion, int idEspecialidad) {
        this.idPractica = idPractica;
        this.codigoNomenclador = codigoNomenclador;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idEspecialidad = idEspecialidad;
    }

    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }
    public String getCodigoNomenclador() { return codigoNomenclador; }
    public void setCodigoNomenclador(String codigoNomenclador) { this.codigoNomenclador = codigoNomenclador; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(int idEspecialidad) { this.idEspecialidad = idEspecialidad; }
}