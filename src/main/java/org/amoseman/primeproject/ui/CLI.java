package org.amoseman.primeproject.ui;

import org.amoseman.primeproject.engine.Engine;
import org.amoseman.primeproject.storage.dao.PrimeDAO;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Scanner;

public class CLI {
    private final Engine engine;
    private final PrimeDAO<?> dao;

    public CLI(Engine engine, PrimeDAO<?> dao) {
        this.engine = engine;
        this.dao = dao;
    }

    public void run() {
        long startTime = System.nanoTime();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.nextLine();
            running = handleInput(input, engine, dao, startTime);
        }
    }

    private boolean handleInput(String input, Engine engine, PrimeDAO<?> dao, long startTime) {
        return switch (input.toUpperCase(Locale.ROOT)) {
            case "EXIT", "QUIT", "STOP" -> {
                engine.stop();
                System.out.println("Stopping...");
                yield false;
            }
            case "STATS" -> {
                long discovered = dao.discovered();
                BigInteger last = dao.last();
                System.out.printf("Total primes discovered: %d\n", discovered);
                System.out.printf("Last prime discovered: %s\n", last);
                yield true;
            }
            case "RUNTIME" -> {
                Runtime runtime = new Runtime(System.nanoTime() - startTime);
                System.out.println(runtime);
                yield true;
            }
            default -> {
                System.out.println("Invalid input");
                yield true;
            }
        };
    }
}
