package com.gagan.fingerprint.fingerprint2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gagan.fingerprint.R;


@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private final Context context;
    private final OnScanFingerPrintInterface onScanFingerprintSuccess;
    private Boolean isAuthenticationSucessfull = false;
    private CancellationSignal cancellationSignal = null;

    public FingerprintHandler(Context context, OnScanFingerPrintInterface onScanFingerprintSuccess) {
        this.context = context;
        this.onScanFingerprintSuccess = onScanFingerprintSuccess;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        //this.update("There was an Auth Error. " + errString, false);
        onScanFingerprintSuccess.onFingerPrintError();
    }

    @Override
    public void onAuthenticationFailed() {
        //this.update("Auth Failed. ", false);
        onScanFingerprintSuccess.onFingerPrintFailed();
        //isAuthenticationSucessfull = false;
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        //this.update("Error: " + helpString, false);
        onScanFingerprintSuccess.onFingerPrintHelp();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        //this.update("You can now access the app.", true);
        onScanFingerprintSuccess.onFingerPrintSuccess();
    }

    private void update(String s, boolean b) {
        TextView paraLabel = (TextView) ((Activity) context).findViewById(R.id.paraLabel);
        ImageView imageView = (ImageView) ((Activity) context).findViewById(R.id.fingerprintImage);

        paraLabel.setText(s);

        if (b == false) {
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            imageView.setImageResource(R.mipmap.action_done);
        }
    }
}
