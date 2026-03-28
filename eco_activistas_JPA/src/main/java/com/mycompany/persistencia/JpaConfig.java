package com.mycompany.persistencia;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase de configuracion de JPA.
 * Gestiona la creacion y cierre del EntityManagerFactory como singleton.
 */
public class JpaConfig {

    private static final String PERSISTENCE_UNIT = "eco_activistas_JPA";
    private static EntityManagerFactory emf;

    private JpaConfig() {}

    /**
     * Retorna la instancia unica del EntityManagerFactory (thread-safe).
     * Se crea la primera vez que se invoca este metodo.
     *
     * @return EntityManagerFactory configurado
     */
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return emf;
    }

    /**
     * Cierra el EntityManagerFactory y libera los recursos de conexion.
     * Debe llamarse al finalizar la aplicacion.
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
