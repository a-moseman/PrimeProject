package org.amoseman.primeproject.engine;

import org.amoseman.primeproject.discovery.BasicPrimeFinder;
import org.amoseman.primeproject.discovery.PrimeFinder;

public class Engine implements Runnable {
    private final PrimeFinder finder;
    private final int batchSize;
    private boolean running;

    public Engine(PrimeFinder finder, int batchSize) {
        this.finder = finder;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            finder.find(batchSize);
        }
    }

    public void stop() {
        running = false;
    }
}
