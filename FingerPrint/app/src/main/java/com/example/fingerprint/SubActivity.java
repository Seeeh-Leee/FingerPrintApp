package com.example.fingerprint;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubActivity extends AppCompatActivity {
    public String note_msg="write some notes";

    TextView textview, note_view;
    EditText password, password_again, note_enter;
    Button enter, home, save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        note_enter = findViewById(R.id.note_enter);
        note_view = findViewById(R.id.note_view);

//      Decryption process
        MasterKey masterkey = null;
        try {
            masterkey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = EncryptedSharedPreferences
                    .create(getApplicationContext(),
                            "filename",
                            masterkey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String saved_note = sharedPreferences.getString("note", "bring the note");
        note_view.setText(saved_note);

//      move to Home screen
        home = findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//      saving the note
        save = findViewById(R.id.save_note);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note_view.setText(note_enter.getText());

//        Encryption process
                MasterKey masterkey = null;
                try {
                    masterkey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                            .build();
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                SharedPreferences sharedPreferences = null;
                try {
                    sharedPreferences = EncryptedSharedPreferences
                            .create(getApplicationContext(),
                                    "filename",
                                    masterkey,
                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                SharedPreferences.Editor spfEditor = sharedPreferences.edit();

                spfEditor.putString("note", note_enter.getText().toString());
                spfEditor.commit();
            }
        });
    }
}
