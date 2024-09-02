package org.amoseman.primeproject;

import org.amoseman.primeproject.discovery.PrimeFinder;

public class Processor implements Runnable {
    private final PrimeFinder finder;
    private final int batchSize;
    private final int chunkSize;
    private boolean running;

    public Processor(PrimeFinder finder, int batchSize, int chunkSize) {
        this.finder = finder;
        this.batchSize = batchSize;
        this.chunkSize = chunkSize;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            finder.find(batchSize, chunkSize);
        }
    }

    public void stop() {
        running = false;
    }
}
