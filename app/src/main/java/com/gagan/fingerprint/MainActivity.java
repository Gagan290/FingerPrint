package com.gagan.fingerprint;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gagan.fingerprint.fingerprint1.BiometricLogin1Activity;
import com.gagan.fingerprint.fingerprint2.BiometricLogin2Activity;
import com.gagan.fingerprint.fingerprint3.BiometricLogin3Activity;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnFingerPrint1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BiometricLogin1Activity.class));
            }
        });

        findViewById(R.id.btnFingerPrint2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BiometricLogin2Activity.class));
            }
        });

        findViewById(R.id.btnFingerPrint3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BiometricLogin3Activity.class));
            }
        });
    }
}
