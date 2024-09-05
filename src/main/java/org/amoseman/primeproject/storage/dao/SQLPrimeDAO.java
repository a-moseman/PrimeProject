package org.amoseman.primeproject.storage.dao;

import org.amoseman.primeproject.storage.init.DatabaseConnection;
import org.jooq.*;
import org.jooq.Record;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class SQLPrimeDAO extends PrimeDAO<DSLContext> {
    private final Table<Record> PRIMES = table("primes");
    private final Field<Object> ID = field("id");
    private final Field<Object> VALUE = field("value");

    public SQLPrimeDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void add(BigInteger prime) {
        connection.get()
                .insertInto(PRIMES, VALUE)
                .values(prime.toByteArray())
                .execute();
        connection.get()
                .update(table("last"))
                .set(VALUE, prime.toByteArray())
                .execute();
    }

    @Override
    public void add(List<byte[]> primes) {
        List<InsertValuesStep1<?, ?>> batch = new ArrayList<>();
        for (byte[] prime : primes) {
            batch.add(connection.get()
                    .insertInto(PRIMES, VALUE)
                    .values(prime)
            );
        }
        connection.get().transaction(ctx -> {
            connection.get().batch(batch).execute();
            connection.get()
                    .update(table("last"))
                    .set(VALUE, primes.get(primes.size() - 1))
                    .execute();
        });
    }

    @Override
    public BigInteger last() {
        Result<Record> result = connection.get()
                .selectFrom(table("last"))
                .fetch();
        if (result.isEmpty()) {
            return null;
        }
        Record record = result.get(0);
        byte[] bytes = (byte[]) record.get(VALUE);
        return new BigInteger(bytes);
    }

    @Override
    public List<BigInteger> get(long offset, long length) {
        Condition range = ID.greaterOrEqual(offset + 1).and(ID.lessThan(offset + length + 1));
        Result<Record> result = connection.get()
                .selectFrom(PRIMES)
                .where(range)
                .fetch();
        List<BigInteger> primes = new ArrayList<>();
        result.forEach(record -> {
            byte[] bytes = (byte[]) record.get(VALUE);
            BigInteger prime = new BigInteger(bytes);
            primes.add(prime);
        });
        return primes;
    }

    @Override
    public long discovered() {
        return connection.get()
                .fetchCount(PRIMES);
    }
}
