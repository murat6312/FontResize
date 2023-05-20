package com.example.fontresize;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Objects;

public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String FONT_SIZE_KEY = "fontSize";
    public static final String ACTION_INCREASE_FONT = "action_increase_font";
    public static final String ACTION_DECREASE_FONT = "action_decrease_font";
    public static final String ACTION_RESET_FONT = "action_reset_font";
    private TextView textView;
    private SharedPreferences preferences;

    private void setFontScale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(this)) {
            float fontSize = preferences.getFloat(FONT_SIZE_KEY, 1f);
            Settings.System.putFloat(getBaseContext().getContentResolver(),
                    Settings.System.FONT_SCALE, fontSize);
            textView.setText("Current Size: " + fontSize + "x");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestWriteSettingsPermission();

        Intent intent = new Intent(this, FontSizeService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        textView = findViewById(R.id.sizeTextView);
        Button increaseTextSizeButton = findViewById(R.id.increaseTextSizeButton);
        Button decreaseTextSizeButton = findViewById(R.id.decreaseTextSizeButton);
        Button doubleSizeButton = findViewById(R.id.doubleSizeButton);
        Button tripleSizeButton = findViewById(R.id.tripleSizeButton);
        Button resetButton = findViewById(R.id.resetButton);

        preferences = getSharedPreferences("FontSizePrefs", MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(this);

        increaseTextSizeButton.setOnClickListener(v -> increaseFontSize());
        decreaseTextSizeButton.setOnClickListener(v -> decreaseFontSize());
        doubleSizeButton.setOnClickListener(v -> preferences.edit().putFloat(FONT_SIZE_KEY, 2f).apply());
        tripleSizeButton.setOnClickListener(v -> preferences.edit().putFloat(FONT_SIZE_KEY, 3f).apply());
        resetButton.setOnClickListener(v -> resetFontSize());

        // Register to receive font size changes.
        LocalBroadcastManager.getInstance(this).registerReceiver(new FontSizeReceiver(), new IntentFilter("FontSizeChange"));
    }

    private void increaseFontSize() {
        float fontSize = preferences.getFloat(FONT_SIZE_KEY, 1f);
        fontSize += 0.2f;
        if (fontSize > 3f) { // Prevent the font size from exceeding 3x its normal size
            fontSize = 3f;
        }
        preferences.edit().putFloat(FONT_SIZE_KEY, fontSize).apply();
    }


    private void decreaseFontSize() {
        float fontSize = preferences.getFloat(FONT_SIZE_KEY, 1f);
        fontSize -= 0.2f; // adjust this value to decrease the speed of size change
        preferences.edit().putFloat(FONT_SIZE_KEY, fontSize).apply();
    }

    private void resetFontSize() {
        preferences.edit().putFloat(FONT_SIZE_KEY, 1f).apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(FONT_SIZE_KEY)) {
            setFontScale();
        }
    }

    private void requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 200);
        }
    }
}
