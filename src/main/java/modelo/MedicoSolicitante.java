package modelo;

/**
 * Representa al médico derivante o solicitante de los estudios médicos.
 */
public class MedicoSolicitante {
    private int idMedicoSolicitante;
    private String nombre;
    private String apellido;
    private String matricula;
    private String especialidad;

    public MedicoSolicitante() {}

    public MedicoSolicitante(int idMedicoSolicitante, String nombre, String apellido, String matricula, String especialidad) {
        this.idMedicoSolicitante = idMedicoSolicitante;
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
        this.especialidad = especialidad;
    }

    /** Permite que el objeto se muestre correctamente dentro de componentes visuales como JComboBox. */
    @Override
    public String toString() {
        return "Mat: " + this.matricula + " - Dr/a. " + this.apellido + ", " + this.nombre;
    }

    // Getters y Setters
    public int getIdMedicoSolicitante() { return idMedicoSolicitante; }
    public void setIdMedicoSolicitante(int idMedicoSolicitante) { this.idMedicoSolicitante = idMedicoSolicitante; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}