package datos;

import conexion.ConexionDB;
import modelo.Practica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PracticaDAO {
    public List<Practica> listar() throws SQLException {
        List<Practica> lista = new ArrayList<>();
        String sql = "SELECT * FROM Practica ORDER BY nombre";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Practica p = new Practica();
                p.setIdPractica(rs.getInt("idPractica"));
                p.setCodigoNomenclador(rs.getString("codigoNomenclador"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setIdEspecialidad(rs.getInt("idEspecialidad"));
                lista.add(p);
            }
        }
        return lista;
    }

    public Practica buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM Practica WHERE codigoNomenclador = ?";
        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Practica p = new Practica();
                    p.setIdPractica(rs.getInt("idPractica"));
                    p.setCodigoNomenclador(rs.getString("codigoNomenclador"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setIdEspecialidad(rs.getInt("idEspecialidad"));
                    return p;
                }
            }
        }
        return null;
    }

    public Practica buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Practica WHERE idPractica = ?";
        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Practica p = new Practica();
                    p.setIdPractica(rs.getInt("idPractica"));
                    p.setCodigoNomenclador(rs.getString("codigoNomenclador"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setIdEspecialidad(rs.getInt("idEspecialidad"));
                    return p;
                }
            }
        }
        return null;
    }
}