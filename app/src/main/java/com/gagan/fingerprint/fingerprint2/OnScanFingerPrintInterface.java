package com.gagan.fingerprint.fingerprint2;

public interface OnScanFingerPrintInterface {
    void onFingerPrintFailed();

    void onFingerPrintError();

    void onFingerPrintHelp();

    void onFingerPrintSuccess();
}
