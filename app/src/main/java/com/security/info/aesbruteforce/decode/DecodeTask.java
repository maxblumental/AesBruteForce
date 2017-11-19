package com.security.info.aesbruteforce.decode;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.security.info.aesbruteforce.BruteForceSettings;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.security.info.aesbruteforce.decode.WordPermutationsImpl.VALID_CHARACTERS;

public class DecodeTask extends AsyncTask<String, Long, DecodingResult> {

    private final BruteForceSettings settings;

    DecodeTask(BruteForceSettings settings) {
        this.settings = settings;
    }

    @Override
    protected DecodingResult doInBackground(String... params) {
        String encoded = params[0];

        WordPermutations wordPermutations = wordPermutations();

        long i = 0;
        long start = System.currentTimeMillis();
        for (String permutation : wordPermutations) {
            String decrypt = decrypt(encoded, permutation);
            if (isValidDecodedMessage(decrypt)) {
                return new DecodingResult(decrypt, permutation, System.currentTimeMillis() - start);
            }
            if (isCancelled()) {
                return null;
            }
            if (i % 1000 == 0) {
                publishProgress(i);
            }
            i++;
        }

        return null;
    }

    private boolean isValidDecodedMessage(String decrypt) {
        if (decrypt == null) {
            return false;
        }
        for (char c : decrypt.toCharArray()) {
            if (!VALID_CHARACTERS.contains(c)) {
                return false;
            }
        }
        return true;
    }

    private WordPermutations wordPermutations() {
        WordPermutations.Builder builder = new WordPermutations.Builder()
                .withLetters(settings.isLetters())
                .withDigits(settings.isDigits())
                .withSpecialCharacters(settings.isSpecialCharacters());
        if (settings.isLengthKnown()) {
            builder.length(settings.getLength());
        }
        return builder.build();
    }

    @Nullable
    private String decrypt(String strToDecrypt, String secret) {
        try {
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec(secret));
            return new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
        } catch (Exception e) {
            return null;
        }
    }


    @Nullable
    private static SecretKeySpec secretKeySpec(String myKey) {
        try {
            byte[] key = myKey.getBytes("UTF-8");
            key = MessageDigest.getInstance("SHA-1").digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            return null;
        }
    }
}
