package org.amoseman.primeproject.storage.dao;

import org.amoseman.primeproject.storage.init.DatabaseConnection;

public class DAO<T> {
    protected DatabaseConnection<T> connection;

    public DAO(DatabaseConnection<T> connection) {
        this.connection = connection;
    }
}
