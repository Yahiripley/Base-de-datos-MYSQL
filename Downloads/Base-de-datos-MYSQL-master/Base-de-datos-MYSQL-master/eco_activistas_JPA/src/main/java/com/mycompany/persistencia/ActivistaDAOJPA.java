package com.mycompany.persistencia;

import com.mycompany.modelo.Activista;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

/**
 * Capa DAO (Data Access Object) que implementa las operaciones CRUD
 * para la entidad Activista usando JPA / Hibernate.
 */
public class ActivistaDAOJPA {

    private final EntityManagerFactory emf;

    public ActivistaDAOJPA() {
        this.emf = JpaConfig.getEntityManagerFactory();
    }

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------

    /**
     * Persiste un nuevo Activista en la base de datos.
     * Tras la operacion, el campo id del objeto queda actualizado con el
     * valor generado por la BD.
     *
     * @param activista objeto a insertar
     * @return true si la operacion fue exitosa, false en caso contrario
     */
    public boolean agregar(Activista activista) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(activista);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error agregar(): " + ex.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // READ
    // -------------------------------------------------------------------------

    /**
     * Busca un Activista por su clave primaria.
     *
     * @param id identificador del activista
     * @return el Activista encontrado, o null si no existe
     */
    public Activista consultar(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Activista.class, id);
        } catch (Exception ex) {
            System.err.println("Error consultar(): " + ex.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Retorna la lista completa de activistas ordenada por ID descendente.
     *
     * @return lista de activistas, o lista vacia si ocurre un error
     */
    public List<Activista> consultarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Activista> query = em.createQuery(
                    "SELECT a FROM Activista a ORDER BY a.id DESC", Activista.class);
            return query.getResultList();
        } catch (Exception ex) {
            System.err.println("Error consultarTodos(): " + ex.getMessage());
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    /**
     * Actualiza los datos de un Activista existente en la base de datos.
     *
     * @param activista objeto con los nuevos valores (debe tener id valido)
     * @return true si la operacion fue exitosa, false en caso contrario
     */
    public boolean actualizar(Activista activista) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(activista);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error actualizar(): " + ex.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    /**
     * Elimina el Activista con el id indicado.
     *
     * @param id identificador del activista a eliminar
     * @return true si se elimino el registro, false si no existia o hubo error
     */
    public boolean eliminar(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Activista activista = em.find(Activista.class, id);
            if (activista == null) {
                em.getTransaction().rollback();
                return false;
            }
            em.remove(activista);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error eliminar(): " + ex.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
