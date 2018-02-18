package com.security.info.aesbruteforce.decode;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.security.info.aesbruteforce.App;
import com.security.info.aesbruteforce.BruteForceSettings;
import com.security.info.aesbruteforce.R;
import com.security.info.aesbruteforce.Utils;
import com.security.info.aesbruteforce.tune.TuneActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

import static com.security.info.aesbruteforce.Utils.hideKeyBoard;
import static java.lang.String.format;

public class DecodeFragment extends Fragment {

  @BindView(R.id.encoded) AppCompatEditText encoded;

  @BindView(R.id.progress_block) View progressBlock;
  @BindView(R.id.progress_bar) ProgressBar progressBar;
  @BindView(R.id.number_of_efforts) TextView numberOfEfforts;

  @BindView(R.id.result_block) View resultBlock;
  @BindView(R.id.decoded) TextView decoded;
  @BindView(R.id.key) TextView key;
  @BindView(R.id.decoding_time) TextView decodingTime;

  @Inject BruteForceSettings settings;

  private DecodeTask decodeTask;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.component.inject(this);
    setHasOptionsMenu(true);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.decode_layout, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.tune:
        startActivity(new Intent(getContext(), TuneActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @OnClick(R.id.decode_button) void onDecodeClick() {
    hideKeyBoard(getActivity());
    if (decodeTask != null && decodeTask.getStatus() == AsyncTask.Status.RUNNING) {
      Toast.makeText(getContext(), "Decoding is already in progress", Toast.LENGTH_SHORT).show();
      return;
    }
    String encoded = this.encoded.getText().toString().trim();
    if (encoded.isEmpty()) {
      return;
    }
    decoded.setText(R.string.decoded_hint);
    resultBlock.setVisibility(View.GONE);
    progressBlock.setVisibility(View.VISIBLE);
    decodeTask = new DecodeTask(settings) {
      @Override
      protected void onPostExecute(DecodingResult result) {
        progressBlock.setVisibility(View.GONE);
        if (result.getMessage() == null) {
          Toast.makeText(getContext(), "Couldn't decode", Toast.LENGTH_SHORT).show();
          return;
        }
        resultBlock.setVisibility(View.VISIBLE);
        decoded.setText(result.getMessage());
        key.setText(result.getKey());
        Date date = new Date(result.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SSS", Locale.ENGLISH);
        decodingTime.setText(formatter.format(date));
      }

      @Override
      protected void onProgressUpdate(Long... values) {
        numberOfEfforts.setText(format(Locale.ENGLISH, "%d", values[0]));
      }

      @Override
      protected void onCancelled(DecodingResult s) {
        progressBlock.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
      }
    };

    decodeTask.execute(encoded);
  }

  @OnClick(R.id.cancel_button) void onCancelClick() {
    if (decodeTask != null) {
      decodeTask.cancel(true);
    }
  }

  @Override public void onDestroyView() {
    if (decodeTask != null) {
      decodeTask.cancel(true);
    }
    super.onDestroyView();
  }
}
