package com.security.info.aesbruteforce.encode;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.security.info.aesbruteforce.R;
import com.security.info.aesbruteforce.Utils;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncodeFragment extends Fragment {

  @BindView(R.id.text_to_encode) EditText textToEncodeView;
  @BindView(R.id.secret) EditText secretView;
  @BindView(R.id.encode_button) Button encodeButton;
  @BindView(R.id.result) TextView resultView;
  @BindView(R.id.copy_button) Button copyButton;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.encode_layout, container, false);
    ButterKnife.bind(this, view);

    copyButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String encrypted = resultView.getText().toString();
        ClipboardManager clipboard =
            (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(encrypted, encrypted);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Copied!", Toast.LENGTH_SHORT).show();
      }
    });

    encodeButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String plain = textToEncodeView.getText().toString();
        String secret = secretView.getText().toString();
        if (plain.isEmpty()) {
          textToEncodeView.setError("Enter text to encode");
          return;
        }
        if (secret.isEmpty()) {
          secretView.setError("Enter secret");
          return;
        }
        String session = String.valueOf(new Random().nextInt());
        String encodedSession = encrypt(session, secret);
        String encodedPlain = encrypt(plain, session);
        resultView.setText(encodedPlain + "\n" + encodedSession);
        copyButton.setVisibility(View.VISIBLE);
        Utils.hideKeyBoard(getActivity());
      }
    });

    return view;
  }

  @Nullable
  private String encrypt(String strToDecrypt, String secret) {
    try {
      @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec(secret));
      return Base64.encodeToString(cipher.doFinal(strToDecrypt.getBytes()), Base64.DEFAULT);
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
