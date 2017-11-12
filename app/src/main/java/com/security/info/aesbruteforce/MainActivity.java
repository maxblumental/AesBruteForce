package com.security.info.aesbruteforce;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.security.info.aesbruteforce.decode.DecodeTask;
import com.security.info.aesbruteforce.decode.DecodingResult;
import com.security.info.aesbruteforce.tune.TuneActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decode_layout);
        App.component.inject(this);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tune:
                startActivity(new Intent(MainActivity.this, TuneActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.decode_button)
    public void onDecodeClick() {
        hideKeyBoard();
        if (decodeTask != null && decodeTask.getStatus() == AsyncTask.Status.RUNNING) {
            Toast.makeText(this, "Decoding is already in progress", Toast.LENGTH_SHORT).show();
            return;
        }
        String encoded = this.encoded.getText().toString();
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
                    Toast.makeText(MainActivity.this, "Couldn't decode", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        };

        decodeTask.execute(encoded);
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClick() {
        if (decodeTask != null) {
            decodeTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        if (decodeTask != null) {
            decodeTask.cancel(true);
        }
        super.onDestroy();
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
