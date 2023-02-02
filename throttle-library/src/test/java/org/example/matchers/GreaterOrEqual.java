package org.example.matchers;

import org.hamcrest.CustomMatcher;

import java.time.Duration;

public final class GreaterOrEqual extends CustomMatcher<Duration> {
    private final Duration value;

    public GreaterOrEqual(Duration value) {
        super(value.toString());
        this.value = value;
    }

    @Override
    public boolean matches(Object o) {
        if (o instanceof Duration start) {
            return start.compareTo(value) >= 0;
        } else {
            return false;
        }
    }
}
