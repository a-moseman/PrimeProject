package org.amoseman.primeproject.storage.service;

import java.math.BigInteger;
import java.util.List;

/**
 * Represents the interface of a prime service.
 */
public interface PrimeService {
    /**
     * Add a prime.
     * @param prime the prime.
     */
    void add(BigInteger prime);

    /**
     * Force the service to write any primes it may have in memory to the database.
     */
    void write();

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
}
