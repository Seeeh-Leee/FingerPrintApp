package com.example.fingerprint;

import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    LinearLayout main_layout;
    private static final int REQUEST_CODE = 101011;

    ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_layout = findViewById(R.id.main_layout);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.image);


        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                            | BiometricManager.Authenticators.DEVICE_CREDENTIAL)){
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(),"Device hasn't fingerprint sensor",
                        Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(),"Device can't use fingerprint sensor",
                        Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(),"No fingerprint assigned",
                        Toast.LENGTH_SHORT).show();
                final Intent enrollintent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollintent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG
                                | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                startActivityForResult(enrollintent, REQUEST_CODE);
            break;
        }

        Executor excutor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                excutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),"Error"+errString,
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull
                                                  BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Login Success",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(),"Fail", Toast.LENGTH_SHORT).show();

            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Finger print")
                .setSubtitle("login with Biometric")
                .setDescription("Use Fingerprint to Login")
                .setNegativeButtonText("Use Password")
                .build();

        imgView.setOnClickListener(imgView->{
            biometricPrompt.authenticate(promptInfo);
        });


    }
}