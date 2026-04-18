package com.biblioteca.dao;

import com.biblioteca.model.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibroDAO {

    private static final Logger LOGGER = Logger.getLogger(LibroDAO.class.getName());
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("bibliotecaPU");

    public boolean insertar(Libro libro) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(libro);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            LOGGER.log(Level.SEVERE, "Error al insertar libro", ex);
            return false;
        } finally {
            em.close();
        }
    }

    public List<Libro> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Libro> query = em.createQuery(
                    "SELECT l FROM Libro l ORDER BY l.id ASC", Libro.class);
            return query.getResultList();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener libros", ex);
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public Libro buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Libro.class, id);
        } finally {
            em.close();
        }
    }

    public boolean actualizar(Libro libro) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(libro);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            LOGGER.log(Level.SEVERE, "Error al actualizar libro", ex);
            return false;
        } finally {
            em.close();
        }
    }

    public boolean eliminar(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Libro libro = em.find(Libro.class, id);
            if (libro == null) {
                tx.rollback();
                return false;
            }
            em.remove(libro);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            LOGGER.log(Level.SEVERE, "Error al eliminar libro", ex);
            return false;
        } finally {
            em.close();
        }
    }

    public static void cerrarEMF() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
