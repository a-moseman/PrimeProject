# Prime Project

A Java 17 project for generating prime numbers. 

## Dependencies
SQLite

Jooq

## Implementation
### Discovery
The list of discovered primes, upon this programs first run, is initialized with the primes 2 and 3. Each prime after is discovered in sequence, skipping over even numbers. Checking for primality is done naively, checking a candidate number n against all primes in the range [3, sqrt(n)], using modulus to determine if it is evenly divisible by any of them. Numbers are representing using [BigInteger](https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html).

### Concurrency
The range of candidate primes that can be checked by an immutable list of pre-discovered primes is [n + 2, n^2], where n is the last discovered prime in the immutable list. Because of this, candidates in that range can be checked in chunks, each with its own thread. The max number of threads and max chunk size per thread are both configurable.

### Database
Each discovered prime is stored in a SQLite database. The database contains two tables: one for the sequential list of primes, and the second for the last prime discovered for fast retrieval. Writes to the database are done in batches of configurable size.

### Cache
For improved retrieval times, the first N discovered primes is stored in memory, where N is the configured size of the cache.

## Testing
To test the program, the first 1000 primes discovered by it are checked against the first 1000 primes pre-determined to be valid.
