package org.amoseman.primeproject.ui;

import org.amoseman.primeproject.engine.Engine;
import org.amoseman.primeproject.storage.dao.PrimeDAO;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Scanner;

public class UI {
    public static void run(Engine engine, PrimeDAO dao) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.nextLine();
            switch (input.toUpperCase(Locale.ROOT)) {
                case "EXIT", "QUIT", "STOP" -> {
                    engine.stop();
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
