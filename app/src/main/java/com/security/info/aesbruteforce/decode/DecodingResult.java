package com.security.info.aesbruteforce.decode;

public class DecodingResult {
    private final String message;
    private final String key;
    private final long time;

    DecodingResult(String message, String key, long time) {
        this.message = message;
        this.key = key;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getKey() {
        return key;
    }

    public long getTime() {
        return time;
    }
}
