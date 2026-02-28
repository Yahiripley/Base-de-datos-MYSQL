import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author josel
 */
public class ActivistaDAO implements IActivistaDAO {

    private final IConexionBD conexion;

    public ActivistaDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean agregar(Activista a) {
        String sql = """
                INSERT INTO activista (nombre, apellido_paterno, apellido_materno, telefono, fecha_inicio_colaboracion)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellidoPaterno());
            ps.setString(3, a.getApellidoMaterno());
            ps.setString(4, a.getTelefono());
            ps.setDate(5, Date.valueOf(a.getFechaInicio()));

            int filas = ps.executeUpdate();
            if (filas == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getInt(1));
            }
            return true;

        } catch (SQLException ex) {
            System.err.println("Error agregar(): " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Activista a) {
        String sql = """
                UPDATE activista
                SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, telefono = ?, fecha_inicio_colaboracion = ?
                WHERE id = ?
                """;
        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellidoPaterno());
            ps.setString(3, a.getApellidoMaterno());
            ps.setString(4, a.getTelefono());
            ps.setDate(5, Date.valueOf(a.getFechaInicio()));
            ps.setInt(6, a.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error actualizar(): " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM activista WHERE id = ?";
        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error eliminar(): " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Activista consultar(int id) {
        String sql = """
                SELECT id, nombre, apellido_paterno, apellido_materno, telefono, fecha_inicio_colaboracion
                FROM activista
                WHERE id = ?
                """;
        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return mapActivista(rs);
            }

        } catch (SQLException ex) {
            System.err.println("Error consultar(): " + ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Activista> consultarTodos() {
        String sql = """
                SELECT id, nombre, apellido_paterno, apellido_materno, telefono, fecha_inicio_colaboracion
                FROM activista
                ORDER BY id DESC
                """;
        List<Activista> lista = new ArrayList<>();

        try (Connection con = conexion.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapActivista(rs));
            }

        } catch (SQLException ex) {
            System.err.println("Error consultarTodos(): " + ex.getMessage());
        }

        return lista;
    }

    private Activista mapActivista(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String apPat = rs.getString("apellido_paterno");
        String apMat = rs.getString("apellido_materno");
        String tel = rs.getString("telefono");
        LocalDate fecha = rs.getDate("fecha_inicio_colaboracion").toLocalDate();
        return new Activista(id, nombre, apPat, apMat, tel, fecha);
    }
}
