package org.amoseman.primeproject.storage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PrimeService {
    private final int maxCacheSize;
    private final int maxDiscoverySize;
    private final PrimeDAO primeDAO;
    private final BigInteger[] cache;
    private int cacheSize;
    private List<byte[]> discovered;

    public PrimeService(int maxCacheSize, int maxDiscoverySize, PrimeDAO primeDAO) {
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

    public void add(BigInteger prime) {
        if (cacheSize < maxCacheSize) {
            cache[cacheSize] = prime;
            cacheSize++;
        }
        discovered.add(prime.toByteArray());
        if (discovered.size() == maxDiscoverySize) {
            forceWrite();
        }
    }

    public void forceWrite() {
        primeDAO.add(discovered);
        discovered.clear();
    }

    public BigInteger last() {
        if (discovered.isEmpty()) {
            return primeDAO.last();
        }
        return new BigInteger(discovered.get(discovered.size() - 1));
    }

    public List<BigInteger> get(final int offset, final int length) {
        List<BigInteger> primes = new ArrayList<>();
        for (int i = offset; i < cacheSize; i++) {
            primes.add(cache[i]);
        }
        if (primes.size() == length) {
            return primes;
        }
        int effectiveOffset = offset + cacheSize;
        int effectiveLength = length - cacheSize;
        primes.addAll(primeDAO.get(effectiveOffset, effectiveLength));
        int i = 0;
        while (primes.size() < length && i < discovered.size()) {
            BigInteger prime = new BigInteger(discovered.get(i));
            primes.add(prime);
        }
        return primes;
    }
}
