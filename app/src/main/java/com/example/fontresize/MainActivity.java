package com.example.fontresize;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private TextView textView;
    private Button increaseTextSizeButton;
    private Button decreaseTextSizeButton;
    private float textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        increaseTextSizeButton = findViewById(R.id.increaseTextSizeButton);
        decreaseTextSizeButton = findViewById(R.id.decreaseTextSizeButton);

        textSize = textView.getTextSize();

        increaseTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize += 2;
                textView.setTextSize(textSize);
            }
        });

        decreaseTextSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize -= 2;
                textView.setTextSize(textSize);
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    private void loadContacts() {
        StringBuilder builder = new StringBuilder();

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String displayName;

                int columnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                if (columnIndex >= 0) {
                    displayName = cursor.getString(columnIndex);
                } else {
                    displayName = "Unknown";
                }

                builder.append(displayName);
                builder.append("\n");
            }
            cursor.close();
        }

        textView.setText(builder.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                textView.setText("Permission must be granted to display contacts");
            }
        }
    }
}