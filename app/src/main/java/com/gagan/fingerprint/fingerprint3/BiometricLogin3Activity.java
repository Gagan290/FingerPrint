package com.gagan.fingerprint.fingerprint3;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gagan.fingerprint.R;

import java.security.KeyStore;

import javax.crypto.Cipher;

@RequiresApi(api = Build.VERSION_CODES.M)
public class BiometricLogin3Activity extends AppCompatActivity {// implements OnScanFingerPrintInterface {
    private final String KEY_NAME = "AndroidKey";
    private TextView mHeadingLabel;
    private ImageView mFingerprintImage;
    private TextView mParaLabel;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private FingerPrintDialog fingerprintDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_login3);

        mHeadingLabel = findViewById(R.id.headingLabel);
        mFingerprintImage = findViewById(R.id.fingerprintImage);
        mParaLabel = findViewById(R.id.paraLabel);


        findViewById(R.id.btnBiometricLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                    keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                    if (!fingerprintManager.isHardwareDetected()) {
                        mParaLabel.setText("Fingerprint Scanner not detected in Device");

                    } else if (ContextCompat.checkSelfPermission(BiometricLogin3Activity.this,
                            Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        mParaLabel.setText("Permission not granted to use Fingerprint Scanner");

                    } else if (!keyguardManager.isKeyguardSecure()) {
                        mParaLabel.setText("Add Lock to your Phone in Settings");

                    } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                        mParaLabel.setText("You should add atleast 1 Fingerprint to use this Feature");

                    } else {
                        mParaLabel.setText("Place your Finger on Scanner to Access the App.");

                        OnScanFingerPrintInterface listener = new OnScanFingerPrintInterface() {

                            @Override
                            public void onFingerPrintFailed() {
                                updateStatus(getResources().getString(R.string.fingerprint_failed));
                                updateLogo("failed");
                            }

                            @Override
                            public void onFingerPrintError(Integer errMsgId, CharSequence errString) {
                                updateStatus(errString.toString());
                                updateLogo("failed");
                            }

                            @Override
                            public void onFingerPrintHelp(Integer helpMsgId, CharSequence helpString) {
                                updateStatus(helpString.toString());
                                updateLogo("failed");
                            }

                            @Override
                            public void onFingerPrintSuccess(FingerprintManager.AuthenticationResult result) {
                                updateStatus("You can now access the app.");
                                updateLogo("success");
                                dismissDialog();
                            }

                            @Override
                            public void onFingerPrintCancel() {
                                updateLogo("");
                                dismissDialog();
                            }

                            @Override
                            public void onNormalLogin() {
                                updateStatus("You can now normal login");
                                updateLogo("");
                                dismissDialog();
                            }
                        };
                        displayFingerPrintDialog(listener);

                        new FingerPrintAuthentication(BiometricLogin3Activity.this)
                                .managerFingerPrint(listener, null);
                    }
                }
            }
        });
    }

    private void displayFingerPrintDialog(OnScanFingerPrintInterface listener) {
        fingerprintDialog = new FingerPrintDialog(this, listener);
        fingerprintDialog.show();
    }

    private void dismissDialog() {
        if (fingerprintDialog != null) {
            fingerprintDialog.dismiss();
        }
    }

    private void updateStatus(String status) {
        if (fingerprintDialog != null) {
            //fingerprintDialog.updateStatus(status);
        }
    }

    private void updateLogo(String status) {
        if (fingerprintDialog != null) {
            fingerprintDialog.updateLogo(status);
        }
    }
}
