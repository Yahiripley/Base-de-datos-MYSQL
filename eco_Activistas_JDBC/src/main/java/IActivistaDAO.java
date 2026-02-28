import java.util.List;

/**
 * 
 * @author josel
 */
public interface IActivistaDAO {
    boolean agregar(Activista activista);
    boolean actualizar(Activista activista);
    boolean eliminar(int id);
    Activista consultar(int id);
    List<Activista> consultarTodos();
}
