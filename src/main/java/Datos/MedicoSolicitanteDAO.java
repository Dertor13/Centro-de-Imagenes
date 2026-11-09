package datos;

import conexion.ConexionDB;
import modelo.MedicoSolicitante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoSolicitanteDAO {

    public List<MedicoSolicitante> listar() throws SQLException {
        List<MedicoSolicitante> lista = new ArrayList<>();
        String sql = "SELECT idMedicoSolicitante, nombre, apellido, matricula, especialidad FROM MedicoSolicitante ORDER BY apellido";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MedicoSolicitante med = new MedicoSolicitante();
                med.setIdMedicoSolicitante(rs.getInt("idMedicoSolicitante"));
                med.setNombre(rs.getString("nombre"));
                med.setApellido(rs.getString("apellido"));
                med.setMatricula(rs.getString("matricula"));
                med.setEspecialidad(rs.getString("especialidad"));
                lista.add(med);
            }
        }
        return lista;
    }

    public MedicoSolicitante buscarPorMatricula(String matricula) throws SQLException {
        String sql = "SELECT * FROM MedicoSolicitante WHERE matricula = ?";
        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MedicoSolicitante m = new MedicoSolicitante();
                    m.setIdMedicoSolicitante(rs.getInt("idMedicoSolicitante"));
                    m.setNombre(rs.getString("nombre"));
                    m.setApellido(rs.getString("apellido"));
                    m.setMatricula(rs.getString("matricula"));
                    m.setEspecialidad(rs.getString("especialidad"));
                    return m;
                }
            }
        }
        return null;
    }

    public MedicoSolicitante buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM MedicoSolicitante WHERE idMedicoSolicitante = ?";
        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MedicoSolicitante m = new MedicoSolicitante();
                    m.setIdMedicoSolicitante(rs.getInt("idMedicoSolicitante"));
                    m.setNombre(rs.getString("nombre"));
                    m.setApellido(rs.getString("apellido"));
                    m.setMatricula(rs.getString("matricula"));
                    m.setEspecialidad(rs.getString("especialidad"));
                    return m;
                }
            }
        }
        return null;
    }

    public boolean registrar(MedicoSolicitante med) throws SQLException {
        String sql = "INSERT INTO MedicoSolicitante (nombre, apellido, matricula, especialidad) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, med.getNombre());
            ps.setString(2, med.getApellido());
            ps.setString(3, med.getMatricula());
            ps.setString(4, med.getEspecialidad());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    public boolean modificar(MedicoSolicitante med) throws SQLException {
        String sql = "UPDATE MedicoSolicitante SET nombre = ?, apellido = ?, especialidad = ? WHERE matricula = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, med.getNombre());
            ps.setString(2, med.getApellido());
            ps.setString(3, med.getEspecialidad());
            ps.setString(4, med.getMatricula());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    public boolean eliminar(String matricula) throws SQLException {
        String sql = "DELETE FROM MedicoSolicitante WHERE matricula = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
}