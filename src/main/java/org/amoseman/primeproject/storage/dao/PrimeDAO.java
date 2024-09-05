package org.amoseman.primeproject.storage.dao;

import org.amoseman.primeproject.storage.init.DatabaseConnection;

import java.math.BigInteger;
import java.util.List;

/**
 * Represents the interface of a prime data access object.
 */
public abstract class PrimeDAO<T> extends DAO<T> {
    public PrimeDAO(DatabaseConnection<T> connection) {
        super(connection);
    }

    /**
     * Add a prime.
     * @param prime the prime.
     */
    public abstract void add(BigInteger prime);

    /**
     * Add a batch of primes.
     * @param primes the batch of primes.
     */
    public abstract void add(List<byte[]> primes);

    /**
     * Get the last prime discovered.
     * @return the last prime discovered.
     */
    public abstract BigInteger last();

    /**
     * Get a list of primes in a range.
     * @param offset the offset of the range.
     * @param length the length of the range.
     * @return the primes in the range.
     */
    public abstract List<BigInteger> get(long offset, long length);

    /**
     * Get the total number of primes discovered.
     * @return the number of primes discovered.
     */
    public abstract long discovered();
}
