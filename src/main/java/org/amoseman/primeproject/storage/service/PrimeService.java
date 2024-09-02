package org.amoseman.primeproject.storage.service;

import java.math.BigInteger;
import java.util.List;

public interface PrimeService {
    void add(BigInteger prime);
    void write();
    BigInteger last();
    List<BigInteger> get(long offset, long length);
}
