package org.amoseman.primeproject.storage.init;

import org.jooq.DSLContext;

import java.math.BigInteger;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

public class SQLDatabaseInitializer extends DatabaseInitializer<DSLContext> {
    public SQLDatabaseInitializer(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void init() {
        boolean empty = connection.get().meta().getTables().isEmpty();
        connection.get()
                .createTableIfNotExists("primes")
                .column(field("id"), BIGINTUNSIGNED.identity(true))
                .column(field("value"), BLOB)
                .constraints(
                        primaryKey(field("id"))
                )
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
