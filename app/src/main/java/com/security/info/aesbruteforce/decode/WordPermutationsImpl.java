package com.security.info.aesbruteforce.decode;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

class WordPermutationsImpl implements WordPermutations {

  private int length;
  private boolean isLengthKnown = false;
  private boolean hasLetters = false;
  private boolean hasDigits = false;
  private boolean hasSpecialCharacters = false;

  private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
  private static final String DIGITS = "0123456789";
  private static final String SPECIAL_CHARACTERS = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

  static final HashSet<Character> VALID_CHARACTERS;

  static {
    VALID_CHARACTERS = new HashSet<>();
    for (char c : LETTERS.toCharArray()) {
      VALID_CHARACTERS.add(c);
    }
    for (char c : DIGITS.toCharArray()) {
      VALID_CHARACTERS.add(c);
    }
    for (char c : SPECIAL_CHARACTERS.toCharArray()) {
      VALID_CHARACTERS.add(c);
    }
  }

  private static final int PASSWORD_MAX_LENGTH = 10;

  WordPermutationsImpl(int length, boolean isLengthKnown,
                       boolean hasLetters,
                       boolean hasDigits,
                       boolean hasSpecialCharacters) {
    this.length = length;
    this.isLengthKnown = isLengthKnown;
    this.hasLetters = hasLetters;
    this.hasDigits = hasDigits;
    this.hasSpecialCharacters = hasSpecialCharacters;
  }

  @Override
  public Iterator<String> iterator() {
    if (isLengthKnown) {
      return new KnownLengthIterator(length);
    }
    return new UnknownLengthIterator();
  }

  private class KnownLengthIterator implements Iterator<String> {

    private final int length;
    private List<Character> alphabet;
    Iterator<ICombinatoricsVector<Character>> generator;

    KnownLengthIterator(int length) {
      this.length = length;
      createAlphabet();
      ICombinatoricsVector<Character> vector = Factory.createVector(alphabet);
      generator = Factory.createPermutationWithRepetitionGenerator(vector, length).iterator();
    }

    private void createAlphabet() {
      StringBuilder builder = new StringBuilder();
      if (hasLetters) {
        builder.append(LETTERS);
      }
      if (hasDigits) {
        builder.append(DIGITS);
      }
      if (hasSpecialCharacters) {
        builder.append(SPECIAL_CHARACTERS);
      }
      char[] chars = builder.toString().toCharArray();
      ArrayList<Character> characters = new ArrayList<>();
      for (char aChar : chars) {
        characters.add(aChar);
      }
      alphabet = characters;
    }

    @Override
    public boolean hasNext() {
      return generator.hasNext();
    }

    @Override
    public String next() {
      ICombinatoricsVector<Character> next = generator.next();
      List<Character> vector = next.getVector();
      StringBuilder builder = new StringBuilder();
      for (Character character : vector) {
        builder.append(character);
      }
      return builder.toString();
    }
  }

  private class UnknownLengthIterator implements Iterator<String> {
    private KnownLengthIterator iterator;

    UnknownLengthIterator() {
      this.iterator = new KnownLengthIterator(1);
    }

    @Override
    public boolean hasNext() {
      if (!iterator.hasNext() && iterator.length < PASSWORD_MAX_LENGTH) {
        iterator = new KnownLengthIterator(iterator.length + 1);
      }
      return iterator.hasNext();
    }

    @Override
    public String next() {
      return iterator.next();
    }
  }
}
