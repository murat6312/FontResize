package com.example.fontresize;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.preference.PreferenceManager;

public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String FONT_SIZE_KEY = "fontSize";
    private TextView textView;
    private Button increaseTextSizeButton;
    private Button decreaseTextSizeButton;
    private Button resetTextSizeButton;
    private Button twoTimesButton;
    private Button threeTimesButton;
    private Button fourTimesButton;
    private SharedPreferences preferences;

    private void setFontScale(float fontScale) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontScale);
        Settings.System.putFloat(getContentResolver(), Settings.System.FONT_SCALE, fontScale);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        increaseTextSizeButton = findViewById(R.id.increaseTextSizeButton);
        decreaseTextSizeButton = findViewById(R.id.decreaseTextSizeButton);
        resetTextSizeButton = findViewById(R.id.resetTextSizeButton);
        twoTimesButton = findViewById(R.id.twoTimesButton);
        threeTimesButton = findViewById(R.id.threeTimesButton);
        fourTimesButton = findViewById(R.id.fourTimesButton);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        increaseTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float fontSize = preferences.getFloat(FONT_SIZE_KEY, 1.0f);
                fontSize += 0.1f;
                preferences.edit().putFloat(FONT_SIZE_KEY, fontSize).apply();
                setFontScale(fontSize);
            }
        });

        decreaseTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float fontSize = preferences.getFloat(FONT_SIZE_KEY, 1.0f);
                fontSize -= 0.1f;
                preferences.edit().putFloat(FONT_SIZE_KEY, fontSize).apply();
                setFontScale(fontSize);
            }
        });

        resetTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putFloat(FONT_SIZE_KEY, 1.0f).apply();
                setFontScale(1.0f);
            }
        });

        twoTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putFloat(FONT_SIZE_KEY, 2.0f).apply();
                setFontScale(2.0f);
            }
        });

        threeTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putFloat(FONT_SIZE_KEY, 3.0f).apply();
                setFontScale(3.0f);
            }
        });

        fourTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putFloat(FONT_SIZE_KEY, 4.0f).apply();
                setFontScale(4.0f);
            }
        });

        float fontSize = preferences.getFloat(FONT_SIZE_KEY, 1.0f);
        setFontScale(fontSize);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(FONT_SIZE_KEY)) {
            float fontSize = sharedPreferences.getFloat(FONT_SIZE_KEY, 1.0f);

            setFontScale(fontSize);
            textView.setText("Current Size: " + fontSize);
        }
    }
}
