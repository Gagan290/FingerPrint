package com.gagan.fingerprint.fingerprint3;

import android.hardware.fingerprint.FingerprintManager;

public interface OnScanFingerPrintInterface {
    void onFingerPrintFailed();

    void onFingerPrintError(Integer errMsgId, CharSequence errString);

    void onFingerPrintHelp(Integer helpMsgId, CharSequence helpString);

    void onFingerPrintSuccess(FingerprintManager.AuthenticationResult result);

    void onFingerPrintCancel();
}
