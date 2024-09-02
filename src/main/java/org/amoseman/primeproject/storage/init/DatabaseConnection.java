package org.amoseman.primeproject.storage.init;

public abstract class DatabaseConnection<T> {
    private final T context;

    public DatabaseConnection(String url) {
        this.context = generateContext(url);
    }

    protected abstract T generateContext(String url);

    public T get() {
        return context;
    }
}
