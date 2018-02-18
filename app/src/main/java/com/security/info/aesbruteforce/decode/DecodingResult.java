package com.security.info.aesbruteforce.decode;

class DecodingResult {
  private final String message;
  private final String key;
  private final long time;

  DecodingResult(String message, String key, long time) {
    this.message = message;
    this.key = key;
    this.time = time;
  }

  String getMessage() {
    return message;
  }

  String getKey() {
    return key;
  }

  long getTime() {
    return time;
  }
}
