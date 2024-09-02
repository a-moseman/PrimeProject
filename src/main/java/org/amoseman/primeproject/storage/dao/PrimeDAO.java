package org.amoseman.primeproject.storage.dao;

import java.math.BigInteger;
import java.util.List;

/**
 * Represents the interface of a prime data access object.
 */
public interface PrimeDAO {
    /**
     * Add a prime.
     * @param prime the prime.
     */
    void add(BigInteger prime);

    /**
     * Add a batch of primes.
     * @param primes the batch of primes.
     */
    void add(List<byte[]> primes);

    /**
     * Get the last prime discovered.
     * @return the last prime discovered.
     */
    BigInteger last();

    /**
     * Get a list of primes in a range.
     * @param offset the offset of the range.
     * @param length the length of the range.
     * @return the primes in the range.
     */
    List<BigInteger> get(long offset, long length);

    /**
     * Get the total number of primes discovered.
     * @return the number of primes discovered.
     */
    long discovered();
}
