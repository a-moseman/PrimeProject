package org.amoseman.primeproject.storage.init;

public abstract class DatabaseInitializer<T> {
    protected final DatabaseConnection<T> connection;

    public DatabaseInitializer(DatabaseConnection<T> connection) {
        this.connection = connection;
    }

    public abstract void init();
}
