package org.amoseman.primeproject.storage.init;

import org.jooq.DSLContext;

import java.math.BigInteger;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.*;

public class DatabaseInitializer {
    private final DatabaseConnection<DSLContext> connection;

    public DatabaseInitializer(DatabaseConnection<DSLContext> connection) {
        this.connection = connection;
    }

    public void init() {
        boolean empty = connection.get().meta().getTables().isEmpty();
        connection.get()
                .createTableIfNotExists("primes")
                .column(field("value"), BLOB)
                .execute();
        connection.get()
                .createTableIfNotExists("last")
                .column(field("value"), BLOB)
                .execute();
        if (empty) {
            connection.get()
                    .insertInto(table("primes"), field("value"))
                    .values(BigInteger.valueOf(2).toByteArray())
                    .execute();
            connection.get()
                    .insertInto(table("primes"), field("value"))
                    .values(BigInteger.valueOf(3).toByteArray())
                    .execute();
            connection.get()
                    .insertInto(table("last"), field("value"))
                    .values(BigInteger.valueOf(3).toByteArray())
                    .execute();
        }
    }
}
