package org.amoseman.primeproject.storage.init;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabaseConnection extends DatabaseConnection<DSLContext> {
    public SQLDatabaseConnection(String url) {
        super(url);
    }

    @Override
    protected DSLContext generateContext(String url) {
        try {
            Connection connection = DriverManager.getConnection(url);
            return DSL.using(connection, SQLDialect.SQLITE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
