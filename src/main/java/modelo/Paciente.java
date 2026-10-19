package modelo;

import java.sql.Date;

/**
 * Entidad Paciente que hereda de Persona (Herencia y Polimorfismo).
 */
public class Paciente extends Persona {
    private int idPaciente;
    private Date fechaNacimiento;
    private String email;
    private Integer idObraSocial;

    public Paciente() {
        super();
    }

    public Paciente(int idPaciente, String dni, String nombre, String apellido, 
                    Date fechaNacimiento, String telefono, String email, Integer idObraSocial) {
        super(dni, nombre, apellido, telefono);
        this.idPaciente = idPaciente;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.idObraSocial = idObraSocial;
    }

    /** Sobrescribe el método abstracto de Persona para cumplir con el polimorfismo. */
    @Override
    public String obtenerDatosCompletos() {
        return getApellido() + ", " + getNombre() + " - DNI: " + getDni();
    }

    // Getters y Setters
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getIdObraSocial() { return idObraSocial; }
    public void setIdObraSocial(Integer idObraSocial) { this.idObraSocial = idObraSocial; }
}