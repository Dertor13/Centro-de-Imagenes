package datos;

import conexion.ConexionDB;
import modelo.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para gestionar la persistencia de Pacientes en MySQL.
 */
public class PacienteDAO {

    public boolean registrar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO Paciente (dni, nombre, apellido, fechaNacimiento, telefono, email, idObraSocial) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, paciente.getDni());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setDate(4, paciente.getFechaNacimiento());
            ps.setString(5, paciente.getTelefono());
            ps.setString(6, paciente.getEmail());

            if (paciente.getIdObraSocial() == null) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, paciente.getIdObraSocial());
            }

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        paciente.setIdPaciente(claves.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public Paciente buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT * FROM Paciente WHERE dni = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paciente p = new Paciente();
                    p.setIdPaciente(rs.getInt("idPaciente"));
                    p.setDni(rs.getString("dni"));
                    p.setNombre(rs.getString("nombre"));
                    p.setApellido(rs.getString("apellido"));
                    p.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setEmail(rs.getString("email"));
                    int idObraSocial = rs.getInt("idObraSocial");
                    p.setIdObraSocial(rs.wasNull() ? null : idObraSocial);
                    return p;
                }
            }
        }
        return null;
    }

    public Paciente buscarPorId(int idPaciente) throws SQLException {
        String sql = "SELECT * FROM Paciente WHERE idPaciente = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paciente p = new Paciente();
                    p.setIdPaciente(rs.getInt("idPaciente"));
                    p.setDni(rs.getString("dni"));
                    p.setNombre(rs.getString("nombre"));
                    p.setApellido(rs.getString("apellido"));
                    p.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setEmail(rs.getString("email"));
                    int idObraSocial = rs.getInt("idObraSocial");
                    p.setIdObraSocial(rs.wasNull() ? null : idObraSocial);
                    return p;
                }
            }
        }
        return null;
    }

    public boolean modificar(Paciente paciente) throws SQLException {
        String sql = "UPDATE Paciente SET dni = ?, nombre = ?, apellido = ?, fechaNacimiento = ?, telefono = ?, email = ?, idObraSocial = ? WHERE idPaciente = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, paciente.getDni());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setDate(4, paciente.getFechaNacimiento());
            ps.setString(5, paciente.getTelefono());
            ps.setString(6, paciente.getEmail());

            if (paciente.getIdObraSocial() == null) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, paciente.getIdObraSocial());
            }

            ps.setInt(8, paciente.getIdPaciente());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idPaciente) throws SQLException {
        String sql = "DELETE FROM Paciente WHERE idPaciente = ?";
        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            return ps.executeUpdate() > 0;
        }
    }
}