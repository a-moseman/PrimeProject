package org.amoseman.primeproject.storage;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final DSLContext context;

    public DatabaseConnection(String url) {
        try {
            Connection connection = DriverManager.getConnection(url);
            context = DSL.using(connection, SQLDialect.SQLITE);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public DSLContext get() {
        return context;
    }
}
