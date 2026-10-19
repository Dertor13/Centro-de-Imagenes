package datos;

import conexion.ConexionDB;
import modelo.ObraSocial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObraSocialDAO {
    public List<ObraSocial> listar() throws SQLException {
        List<ObraSocial> lista = new ArrayList<>();
        String sql = "SELECT idobraSocial, nombre FROM Obrasocial ORDER BY nombre";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ObraSocial os = new ObraSocial();
                os.setIdObraSocial(rs.getInt("idobraSocial"));
                os.setNombre(rs.getString("nombre"));
                lista.add(os);
            }
        }
        return lista;
    }
}