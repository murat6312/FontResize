package com.example.fontresize;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Objects;

public class FontSizeReceiver extends BroadcastReceiver {
    private static final String FONT_SIZE_KEY = "fontSize";
    public static final String ACTION_INCREASE_FONT = "com.example.fontresize.INCREASE_FONT";
    public static final String ACTION_DECREASE_FONT = "com.example.fontresize.DECREASE_FONT";
    public static final String ACTION_RESET_FONT = "com.example.fontresize.RESET_FONT";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("FontSizePrefs", Context.MODE_PRIVATE);
        float fontSize = preferences.getFloat(FONT_SIZE_KEY, 1f);

        switch (Objects.requireNonNull(intent.getAction())) {
            case ACTION_INCREASE_FONT:
                fontSize += 0.2f;
                if (fontSize > 3f) { // Prevent the font size from exceeding 3x its normal size
                    fontSize = 3f;
                }
                break;
            case ACTION_DECREASE_FONT:
                fontSize -= 0.2f;
                if (fontSize < 1f) fontSize = 1f;
                break;
            case ACTION_RESET_FONT:
                fontSize = 1f;
                break;
        }

        preferences.edit().putFloat(FONT_SIZE_KEY, fontSize).apply();
    }

}
