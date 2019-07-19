package com.sample.rest.server.core;

import java.time.LocalDateTime;

public class TimeProvider {
    public long getTimeInMillis() {
        return System.currentTimeMillis();
    }

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
