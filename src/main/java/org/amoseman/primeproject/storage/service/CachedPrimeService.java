package org.amoseman.primeproject.storage.service;

import org.amoseman.primeproject.storage.dao.PrimeDAO;
import org.jooq.DSLContext;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CachedPrimeService implements PrimeService {
    private final int maxCacheSize;
    private final int maxDiscoverySize;
    private final PrimeDAO<DSLContext> primeDAO;
    private final BigInteger[] cache;
    private int cacheSize;
    private final List<byte[]> discovered;

    public CachedPrimeService(int maxCacheSize, int maxDiscoverySize, PrimeDAO<DSLContext> primeDAO) {
        this.maxCacheSize = maxCacheSize;
        this.maxDiscoverySize = maxDiscoverySize;
        this.primeDAO = primeDAO;
        this.cache = new BigInteger[maxCacheSize];
        this.cacheSize = 0;
        List<BigInteger> primes = primeDAO.get(0, maxCacheSize);
        for (int i = 0; i < primes.size(); i++) {
            cache[i] = primes.get(i);
            cacheSize++;
        }
        this.discovered = new ArrayList<>();
    }

    @Override
    public void add(BigInteger prime) {
        if (cacheSize < maxCacheSize) {
            cache[cacheSize] = prime;
            cacheSize++;
        }
        discovered.add(prime.toByteArray());
        if (discovered.size() == maxDiscoverySize) {
            write();
        }
    }

    @Override
    public void write() {
        primeDAO.add(discovered);
        discovered.clear();
    }

    @Override
    public BigInteger last() {
        if (discovered.isEmpty()) {
            return primeDAO.last();
        }
        return new BigInteger(discovered.get(discovered.size() - 1));
    }

    @Override
    public List<BigInteger> get(long offset, long length) {
        List<BigInteger> primes = new ArrayList<>(Arrays.asList(cache).subList((int) offset, cacheSize));
        if (primes.size() == length) {
            return primes;
        }
        long effectiveOffset = offset + cacheSize;
        long effectiveLength = length - cacheSize;
        primes.addAll(primeDAO.get(effectiveOffset, effectiveLength));
        int i = 0;
        while (primes.size() < length && i < discovered.size()) {
            BigInteger prime = new BigInteger(discovered.get(i));
            primes.add(prime);
        }
        return primes;
    }

    @Override
    public BigInteger get(long index) {
        if (index < cacheSize) {
            return cache[(int) index];
        }
        List<BigInteger> primes = primeDAO.get(index, 1);
        if (primes.size() == 1) {
            return primes.get(0);
        }
        long effectiveIndex = index - primeDAO.discovered();
        if (effectiveIndex < discovered.size()) {
            return new BigInteger(discovered.get((int) effectiveIndex));
        }
        return null;
    }
}
