package modelo;

import java.sql.Date;
import java.sql.Time;

/**
 * Entidad central del sistema que vincula a un paciente, una práctica y un médico en una fecha y hora determinada.
 */
public class Turno {
    private int idTurno;
    private Date fecha;
    private Time hora;
    private String estado;      // Ej: 'PENDIENTE', 'CONFIRMADO', 'CANCELADO'
    private String asistencia;  // Ej: 'PENDIENTE', 'ASISTIO', 'NO_ASISTIO'
    private int idPaciente;
    private int idPractica;
    private int idMedicoSolicitante;

    public Turno() {
    }

    public Turno(int idTurno, Date fecha, Time hora, String estado, String asistencia, 
                   int idPaciente, int idPractica, int idMedicoSolicitante) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = (estado != null) ? estado : "PENDIENTE";
        this.asistencia = (asistencia != null) ? asistencia : "PENDIENTE";
        this.idPaciente = idPaciente;
        this.idPractica = idPractica;
        this.idMedicoSolicitante = idMedicoSolicitante;
    }

    // Getters y Setters
    public int getIdTurno() { return idTurno; }
    public void setIdTurno(int idTurno) { this.idTurno = idTurno; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Time getHora() { return hora; }
    public void setHora(Time hora) { this.hora = hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getAsistencia() { return asistencia; }
    public void setAsistencia(String asistencia) { this.asistencia = asistencia; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }

    public int getIdMedicoSolicitante() { return idMedicoSolicitante; }
    public void setIdMedicoSolicitante(int idMedicoSolicitante) { this.idMedicoSolicitante = idMedicoSolicitante; }
}