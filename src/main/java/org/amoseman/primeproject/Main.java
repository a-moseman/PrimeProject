package org.amoseman.primeproject;

import org.amoseman.primeproject.discovery.PrimeFinder;
import org.amoseman.primeproject.engine.Engine;
import org.amoseman.primeproject.storage.init.DatabaseConnection;
import org.amoseman.primeproject.storage.init.DatabaseInitializer;
import org.amoseman.primeproject.storage.init.SQLDatabaseConnection;
import org.amoseman.primeproject.storage.init.SQLDatabaseInitializer;
import org.amoseman.primeproject.storage.dao.PrimeDAO;
import org.amoseman.primeproject.storage.dao.SQLPrimeDAO;
import org.amoseman.primeproject.storage.service.CachedPrimeService;
import org.amoseman.primeproject.storage.service.PrimeService;
import org.amoseman.primeproject.ui.UI;
import org.jooq.DSLContext;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection<DSLContext> connection = new SQLDatabaseConnection("jdbc:sqlite:primes.db");
        DatabaseInitializer<DSLContext> initializer = new SQLDatabaseInitializer(connection);
        initializer.init();
        PrimeDAO primeDAO = new SQLPrimeDAO(connection);
        PrimeService primeService = new CachedPrimeService(64_000, 800, primeDAO);
        PrimeFinder finder = new PrimeFinder(primeService);

        Engine engine = new Engine(finder, 256);
        Thread thread = new Thread(engine);
        thread.start();
        UI.run(engine, primeDAO);
    }
}
