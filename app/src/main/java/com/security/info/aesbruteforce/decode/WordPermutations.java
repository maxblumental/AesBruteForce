package com.security.info.aesbruteforce.decode;

interface WordPermutations extends Iterable<String> {

  class Builder {

    private int length;
    private boolean isLengthKnown = false;
    private boolean hasLetters = false;
    private boolean hasDigits = false;
    private boolean hasSpecialCharacters = false;

    Builder length(int length) {
      this.isLengthKnown = true;
      this.length = length;
      return this;
    }

    Builder withLetters(boolean withLetters) {
      this.hasLetters = withLetters;
      return this;
    }

    Builder withDigits(boolean withDigits) {
      this.hasDigits = withDigits;
      return this;
    }

    Builder withSpecialCharacters(boolean withSpecialCharacters) {
      this.hasSpecialCharacters = withSpecialCharacters;
      return this;
    }

    WordPermutations build() {
      return new WordPermutationsImpl(length, isLengthKnown,
          hasLetters, hasDigits, hasSpecialCharacters);
    }
  }
}
