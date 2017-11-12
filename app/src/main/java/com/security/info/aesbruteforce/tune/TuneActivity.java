package com.security.info.aesbruteforce.tune;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;

import com.security.info.aesbruteforce.App;
import com.security.info.aesbruteforce.BruteForceSettings;
import com.security.info.aesbruteforce.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class TuneActivity extends AppCompatActivity implements TextWatcher {

    @Inject BruteForceSettings settings;

    @BindView(R.id.length_known_checkbox) CheckBox lengthKnownCheckbox;
    @BindView(R.id.length) AppCompatEditText lengthEditText;
    @BindView(R.id.length_picker_block) View lengthPickerBlock;

    @BindView(R.id.letters_checkbox) CheckBox lettersCheckbox;
    @BindView(R.id.digits_checkbox) CheckBox digitsCheckbox;
    @BindView(R.id.special_characters_checkbox) CheckBox specialCharactersCheckbox;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decode_settings_layout);
        ButterKnife.bind(this);
        App.component.inject(this);

        lengthKnownCheckbox.setChecked(settings.isLengthKnown());
        lengthEditText.setText(Integer.toString(settings.getLength()));

        lettersCheckbox.setChecked(settings.isLetters());
        digitsCheckbox.setChecked(settings.isDigits());
        specialCharactersCheckbox.setChecked(settings.isSpecialCharacters());

        lengthEditText.addTextChangedListener(this);
    }

    @OnCheckedChanged(R.id.length_known_checkbox)
    public void onLengthKnownClick(boolean checked) {
        settings.setLengthKnown(checked);
        lengthPickerBlock.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    @OnCheckedChanged(R.id.letters_checkbox)
    public void onLettersClicked(boolean checked) {
        settings.setLetters(checked);
    }

    @OnCheckedChanged(R.id.digits_checkbox)
    public void onDigitsClicked(boolean checked) {
        settings.setDigits(checked);
    }

    @OnCheckedChanged(R.id.special_characters_checkbox)
    public void onSpecialCharactersClicked(boolean checked) {
        settings.setSpecialCharacters(checked);
    }

    @Override
    public void onDestroy() {
        lengthEditText.removeTextChangedListener(this);
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = s.toString();
        if (!input.isEmpty()) {
            settings.setLength(Integer.parseInt(input));
        }
    }
}
