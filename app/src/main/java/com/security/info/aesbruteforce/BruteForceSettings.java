package com.security.info.aesbruteforce;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BruteForceSettings {

  private volatile boolean lengthKnown;
  private volatile int length;

  private volatile boolean letters;
  private volatile boolean digits;
  private volatile boolean specialCharacters;

  @Inject BruteForceSettings() {
    this.lengthKnown = false;
    this.length = 0;

    this.letters = true;
    this.digits = true;
    this.specialCharacters = true;
  }

  public boolean isLengthKnown() {
    return lengthKnown;
  }

  public void setLengthKnown(boolean lengthKnown) {
    this.lengthKnown = lengthKnown;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public boolean isLetters() {
    return letters;
  }

  public void setLetters(boolean letters) {
    this.letters = letters;
  }

  public boolean isDigits() {
    return digits;
  }

  public void setDigits(boolean digits) {
    this.digits = digits;
  }

  public boolean isSpecialCharacters() {
    return specialCharacters;
  }

  public void setSpecialCharacters(boolean specialCharacters) {
    this.specialCharacters = specialCharacters;
  }
}
