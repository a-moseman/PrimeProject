package org.amoseman.primeproject.storage;

import org.jooq.*;
import org.jooq.Record;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class PrimeDAO {
    private final Table<Record> PRIMES = table("primes");
    private final Field<Object> VALUE = field("value");
    private final DatabaseConnection connection;

    public PrimeDAO(DatabaseConnection connection) {
        this.connection = connection;
    }

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

    public void add(List<byte[]> primes) {
        List<InsertValuesStep1<?, ?>> batch = new ArrayList<>();
        for (byte[] prime : primes) {
            batch.add(connection.get()
                    .insertInto(PRIMES, VALUE)
                    .values(prime)
            );
        }
        connection.get().batch(batch).execute();
        connection.get()
                .update(table("last"))
                .set(VALUE, primes.get(primes.size() - 1))
                .execute();
    }

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

    public List<BigInteger> get(int offset, int length) {
        Result<Record> result = connection.get()
                .selectFrom(PRIMES)
                .limit(length)
                .offset(offset)
                .fetch();
        List<BigInteger> primes = new ArrayList<>();
        result.forEach(record -> {
            byte[] bytes = (byte[]) record.get(VALUE);
            BigInteger prime = new BigInteger(bytes);
            primes.add(prime);
        });
        return primes;
    }

    public long discovered() {
        return connection.get()
                .fetchCount(PRIMES);
    }
}
