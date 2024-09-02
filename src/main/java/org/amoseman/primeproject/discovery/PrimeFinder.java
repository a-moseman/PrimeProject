package org.amoseman.primeproject.discovery;

import org.amoseman.primeproject.storage.service.PrimeService;

import java.math.BigInteger;

public class PrimeFinder {
    private final PrimeService primeService;

    public PrimeFinder(PrimeService primeService) {
        this.primeService = primeService;
    }

    public void find(int n) {
        int discovered = 0;
        BigInteger number = primeService.last().add(BigInteger.TWO);
        while (discovered < n) {
            if (isPrime(number)) {
                primeService.add(number);
                discovered++;
            }
            number = number.add(BigInteger.TWO);
        }
        primeService.write();
    }

    private boolean isPrime(BigInteger number) {
        BigInteger sqrt = number.sqrt();
        int offset = 1;
        BigInteger prime;
        while ((prime = primeService.get(offset)) != null) {
            if (prime.compareTo(sqrt) > 0) {
                return true;
            }
            if (number.mod(prime).compareTo(BigInteger.ZERO) == 0) {
                return false;
            }
            offset++;
        }
        return true;
    }
}
