import java.sql.Connection;
import java.sql.SQLException;


/**
 * 
 * @author josel
 */
public interface IConexionBD {
    Connection crearConexion() throws SQLException;
}
