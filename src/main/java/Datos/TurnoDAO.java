package datos;

import conexion.ConexionDB;
import modelo.Turno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

/**
 * Data Access Object para la gestión de turnos y generación de grillas horarias.
 */
public class TurnoDAO {

    public boolean registrar(Turno turno) throws SQLException {
        String sql = "INSERT INTO Turno (fecha, hora, estado, asistencia, idPaciente, idPractica, idMedicoSolicitante) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, turno.getFecha());
            ps.setTime(2, turno.getHora());
            ps.setString(3, turno.getEstado());
            ps.setString(4, turno.getAsistencia());
            ps.setInt(5, turno.getIdPaciente());
            ps.setInt(6, turno.getIdPractica());
            ps.setInt(7, turno.getIdMedicoSolicitante());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        turno.setIdTurno(claves.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public boolean modificar(Turno turno) throws SQLException {
        String sql = "UPDATE Turno SET estado = ?, asistencia = ?, idPaciente = ?, idPractica = ?, idMedicoSolicitante = ? WHERE idTurno = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, turno.getEstado());
            ps.setString(2, turno.getAsistencia());
            ps.setInt(3, turno.getIdPaciente());
            ps.setInt(4, turno.getIdPractica());
            ps.setInt(5, turno.getIdMedicoSolicitante());
            ps.setInt(6, turno.getIdTurno());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean cambiarEstado(int idTurno, String estado, String asistencia) throws SQLException {
        String sql = "UPDATE Turno SET estado = ?, asistencia = ? WHERE idTurno = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setString(2, asistencia);
            ps.setInt(3, idTurno);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idTurno) throws SQLException {
        String sql = "DELETE FROM Turno WHERE idTurno = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idTurno);
            return ps.executeUpdate() > 0;
        }
    }

    public Turno buscarPorId(int idTurno) throws SQLException {
        String sql = "SELECT idTurno, fecha, hora, estado, asistencia, idPaciente, idPractica, idMedicoSolicitante FROM Turno WHERE idTurno = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTurno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Turno t = new Turno();
                    t.setIdTurno(rs.getInt("idTurno"));
                    t.setFecha(rs.getDate("fecha"));
                    t.setHora(rs.getTime("hora"));
                    t.setEstado(rs.getString("estado"));
                    t.setAsistencia(rs.getString("asistencia"));
                    t.setIdPaciente(rs.getInt("idPaciente"));
                    t.setIdPractica(rs.getInt("idPractica"));
                    t.setIdMedicoSolicitante(rs.getInt("idMedicoSolicitante"));
                    return t;
                }
            }
        }
        return null;
    }

    /** Genera una grilla horaria fija de 07:00 a 19:00 horas, cruzando los turnos reservados con la base de datos. */
    public List<Object[]> obtenerGrillaPorFechaYEspecialidad(Date fecha, int idEspecialidad) throws SQLException {
        List<Object[]> grillaFinal = new ArrayList<>();
        Map<String, Object[]> turnosReservados = new HashMap<>();

        String sqlTurnos = "SELECT t.idTurno, TIME_FORMAT(t.hora, '%H:%i') AS hora_str, "
                         + "CONCAT(p.apellido, ', ', LEFT(p.nombre, 1), '.') AS paciente, "
                         + "pr.nombre AS practica, "
                         + "CONCAT('Dr/a. ', m.apellido) AS derivante, "
                         + "t.estado "
                         + "FROM Turno t "
                         + "INNER JOIN Paciente p ON t.idPaciente = p.idPaciente "
                         + "INNER JOIN Practica pr ON t.idPractica = pr.idPractica "
                         + "INNER JOIN MedicoSolicitante m ON t.idMedicoSolicitante = m.idMedicoSolicitante "
                         + "WHERE t.fecha = ? AND pr.idEspecialidad = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlTurnos)) {
            ps.setDate(1, fecha);
            ps.setInt(2, idEspecialidad);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String horaStr = rs.getString("hora_str");
                    if (horaStr != null && horaStr.length() >= 5) {
                        horaStr = horaStr.substring(0, 5);
                    }
                    
                    Object[] datosTurno = new Object[]{
                        rs.getInt("idTurno"),
                        horaStr,
                        rs.getString("paciente"),
                        rs.getString("practica"),
                        rs.getString("derivante"),
                        rs.getString("estado")
                    };
                    turnosReservados.put(horaStr, datosTurno);
                }
            }
        }

        // Rellenar slots de 07:00 a 19:00
        LocalTime horaActual = LocalTime.of(7, 0);
        LocalTime horaFin = LocalTime.of(19, 0);

        while (!horaActual.isAfter(horaFin)) {
            String horaFormateada = horaActual.format(DateTimeFormatter.ofPattern("HH:mm"));

            if (turnosReservados.containsKey(horaFormateada)) {
                grillaFinal.add(turnosReservados.get(horaFormateada));
            } else {
                Object[] slotLibre = new Object[]{
                    -1,
                    horaFormateada,
                    "[Disponible]",
                    "---",
                    "---",
                    "Libre"
                };
                grillaFinal.add(slotLibre);
            }
            horaActual = horaActual.plusHours(1);
        }

        return grillaFinal;
    }
}