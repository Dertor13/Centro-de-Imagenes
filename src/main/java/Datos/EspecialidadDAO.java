package datos;

import conexion.ConexionDB;
import modelo.Especialidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAO {
    public List<Especialidad> listar() throws SQLException {
        List<Especialidad> lista = new ArrayList<>();
        String sql = "SELECT idEspecialidad, nombre, descripcion FROM Especialidad ORDER BY nombre";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setIdEspecialidad(rs.getInt("idEspecialidad"));
                esp.setNombre(rs.getString("nombre"));
                esp.setDescripcion(rs.getString("descripcion"));
                lista.add(esp);
            }
        }
        return lista;
    }
}