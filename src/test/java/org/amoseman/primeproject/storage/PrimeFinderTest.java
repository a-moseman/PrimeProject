package org.amoseman.primeproject.storage;

import org.amoseman.primeproject.discovery.PrimeFinder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

class PrimeFinderTest {

    @Test
    void test() {
        int count = 200;
        /**
         * BELOW THIS IS CODE THAT SHOULD NOT CHANGE.
         * IT MUST GENERATE VALID PRIME NUMBERS IN ORDER.
         */
        List<Integer> expected = new ArrayList<>();
        expected.add(2);
        expected.add(3);
        int current = 3;
        while (expected.size() < count) {
            current += 2;
            int sqrt = (int) (Math.sqrt(current) + 0.5);
            boolean isPrime = true;
            for (int i = 1; i < expected.size(); i++) {
                int prime = expected.get(i);
                if (prime > sqrt) {
                    break;
                }
                if (current % prime == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                expected.add(current);
            }
        }
        /**
         * ABOVE THIS IS CODE THAT SHOULD NOT CHANGE.
         * IT MUST GENERATE VALID PRIME NUMBERS IN ORDER.
         */
        new File("test.db").deleteOnExit();
        DatabaseConnection connection = new DatabaseConnection("jdbc:sqlite:test.db");
        DatabaseInitializer initializer = new DatabaseInitializer(connection);
        initializer.init();
        PrimeDAO dao = new PrimeDAO(connection);

        List<BigInteger> first = dao.get(0, 2);
        if (!(first.contains(BigInteger.TWO) && first.contains(BigInteger.valueOf(3)))) {
            fail("Does not contain starting 2 and 3");
        }

        PrimeService service = new PrimeService(69, 36, dao);
        PrimeFinder finder = new PrimeFinder(service);
        finder.find(count - 2, 46);
        // compare actual to expected
        List<BigInteger> actualService = service.get(0, count);
        List<BigInteger> actualDAO = dao.get(0, count);
        if (actualService.size() != actualDAO.size()) {
            fail(String.format("Service and DAO sizes are different: %d and %d respectively", actualService.size(), actualDAO.size()));
        }
        for (int i = 0; i < actualService.size(); i++) {
            if (actualService.get(i).compareTo(actualDAO.get(i)) != 0) {
                fail(String.format("Service value different than DAO value: %d and %d at %d respectively", actualService.get(i), actualDAO.get(i), i));
            }
        }
        if (expected.size() != actualService.size()) {
            fail(String.format("Found different number of primes than expected. Expected %d, got %d", expected.size(), actualService.size()));
        }
        for (int i = 0; i < expected.size(); i++) {
            if (BigInteger.valueOf(expected.get(i)).compareTo(actualService.get(i)) != 0) {
                fail(String.format("Actual prime different than expected. Expected %d, got %d, at index %d", expected.get(i), actualService.get(i), i));
            }
        }
    }
}