package org.amoseman.primeproject.storage.dao;

import org.amoseman.primeproject.storage.init.DatabaseConnection;

/**
 * Represents a data access object.
 * @param <T> the client of the object.
 */
public class DAO<T> {
    protected DatabaseConnection<T> connection;

    /**
     * Instantiate a new data access object.
     * @param connection the connection of the object.
     */
    public DAO(DatabaseConnection<T> connection) {
        this.connection = connection;
    }
}
