package org.amoseman.primeproject.discovery;

import org.amoseman.primeproject.storage.service.PrimeService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ThreadedPrimeFinder implements PrimeFinder {
    private final PrimeService service;
    private final int maxThreads;
    private final long maxRange;
    private final ExecutorService executor;

    public ThreadedPrimeFinder(PrimeService service, int maxThreads, long maxRange) {
        this.service = service;
        this.maxThreads = maxThreads;
        this.maxRange = maxRange;
        this.executor = Executors.newCachedThreadPool();
    }

    private BigInteger clone(BigInteger number) {
        return number.add(BigInteger.ZERO);
    }

    @Override
    public void find(long n) {
        int m = 0;
        while (m < n) {
            int t = 0;
            BigInteger last = service.last();
            BigInteger minCandidate = last.add(BigInteger.TWO);
            BigInteger maxCandidate = last.pow(2);

            BigInteger min = clone(minCandidate);
            BigInteger max;
            List<Future<List<BigInteger>>> futures = new ArrayList<>();
            do {
                max = min.add(BigInteger.valueOf(maxRange));
                max = max.min(maxCandidate);
                Worker worker = new Worker(service, min, max);
                Future<List<BigInteger>> future = executor.submit(worker);
                futures.add(future);
                t++;
                min = max;
            }
            while (max.compareTo(maxCandidate) < 0 && t < maxThreads);
            List<BigInteger> discovered = new ArrayList<>();
            futures.forEach(future -> {
                try {
                    discovered.addAll(future.get());
                }
                catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            discovered.forEach(service::add);
            service.write();
            m += discovered.size();
        }
    }

    static class Worker implements Callable<List<BigInteger>> {
        private final PrimeService service;
        private final BigInteger min;
        private final BigInteger max;

        public Worker(PrimeService service, BigInteger min, BigInteger max) {
            this.service = service;
            this.min = min;
            this.max = max;
        }

        private boolean isPrime(BigInteger number) {
            BigInteger sqrt = number.sqrt();
            int offset = 1;
            BigInteger prime;
            while ((prime = service.get(offset)) != null) {
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

        @Override
        public List<BigInteger> call() throws Exception {
            List<BigInteger> discovered = new ArrayList<>();
            BigInteger current = min.add(BigInteger.ZERO);
            while (current.compareTo(max) < 0) {
                if (isPrime(current)) {
                    discovered.add(current);
                }
                current = current.add(BigInteger.TWO);
            }
            return discovered;
        }
    }
}
