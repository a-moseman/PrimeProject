package org.amoseman.primeproject.ui;

public class Runtime {
    public static final long SECONDS_PER_MINUTE = 60;
    public static final long MINUTES_PER_HOUR = 60;
    public static final long NANOSECONDS_PER_SECOND = 1_000_000_000;
    public static final long NANOSECONDS_PER_MINUTE = NANOSECONDS_PER_SECOND * SECONDS_PER_MINUTE;
    public static final long NANOSECONDS_PER_HOUR = NANOSECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    public final long runtime;
    public final long hours;
    public final long minutes;
    public final long seconds;

    public Runtime(long runtime) {
        this.runtime = runtime;
        this.hours = runtime / NANOSECONDS_PER_HOUR;
        this.minutes = runtime / NANOSECONDS_PER_MINUTE - hours * MINUTES_PER_HOUR;
        this.seconds = runtime / NANOSECONDS_PER_SECOND - minutes * SECONDS_PER_MINUTE;
    }

    private String pad(long number) {
        if (9 < number) {
            return String.valueOf(number);
        }
        return '0' + String.valueOf(number);
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s\n", pad(hours), pad(minutes), pad(hours));
    }
}
