package org.amoseman.primeproject;

import org.amoseman.primeproject.discovery.PrimeFinder;
import org.amoseman.primeproject.storage.init.DatabaseConnection;
import org.amoseman.primeproject.storage.init.DatabaseInitializer;
import org.amoseman.primeproject.storage.init.SQLDatabaseConnection;
import org.amoseman.primeproject.storage.init.SQLDatabaseInitializer;
import org.amoseman.primeproject.storage.dao.PrimeDAO;
import org.amoseman.primeproject.storage.dao.SQLPrimeDAO;
import org.amoseman.primeproject.storage.service.CachedPrimeService;
import org.amoseman.primeproject.storage.service.PrimeService;
import org.jooq.DSLContext;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection<DSLContext> connection = new SQLDatabaseConnection("jdbc:sqlite:primes.db");
        DatabaseInitializer<DSLContext> initializer = new SQLDatabaseInitializer(connection);
        initializer.init();
        PrimeDAO primeDAO = new SQLPrimeDAO(connection);
        PrimeService primeService = new CachedPrimeService(64_000, 16_000, primeDAO);
        PrimeFinder finder = new PrimeFinder(primeService);

        Processor processor = new Processor(finder, 256, 8_000);
        Thread thread = new Thread(processor);
        thread.start();
        io(processor, primeDAO);
    }

    private static void io(Processor processor, PrimeDAO dao) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.nextLine();
            switch (input.toUpperCase(Locale.ROOT)) {
                case "EXIT", "QUIT", "STOP" -> {
                    processor.stop();
                    System.out.println("Stopping...");
                    running = false;
                }
                case "STATS" -> {
                    long discovered = dao.discovered();
                    BigInteger last = dao.last();
                    System.out.printf("Total primes discovered: %d\n", discovered);
                    System.out.printf("Last prime discovered: %s\n", last);
                }
            }
        }
    }
}
