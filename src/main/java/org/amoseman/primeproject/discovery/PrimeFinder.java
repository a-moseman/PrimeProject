package org.amoseman.primeproject.discovery;

import org.amoseman.primeproject.storage.service.PrimeService;

import java.math.BigInteger;
import java.util.List;

public class PrimeFinder {
    private final PrimeService primeService;

    public PrimeFinder(PrimeService primeService) {
        this.primeService = primeService;
    }

    public void find(int n, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Chunk size must be greater than 0");
        }
        int discovered = 0;
        BigInteger number = primeService.last().add(BigInteger.TWO);
        while (discovered < n) {
            if (isPrime(number, chunkSize)) {
                primeService.add(number);
                discovered++;
            }
            number = number.add(BigInteger.TWO);
        }
        primeService.write();
    }

    private boolean isPrime(BigInteger number, int chunkSize) {
        BigInteger sqrt = number.sqrt();
        int offset = 1;
        List<BigInteger> primes;
        while (!(primes = primeService.get(offset, chunkSize)).isEmpty()) {
            for (BigInteger prime : primes) {
                if (prime.compareTo(sqrt) > 0) {
                    return true;
                }
                if (number.mod(prime).compareTo(BigInteger.ZERO) == 0) {
                    return false;
                }
            }
            offset += chunkSize;
        }
        return true;
    }
}
