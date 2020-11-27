package com.gagan.fingerprint.fingerprint2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.gagan.fingerprint.MainActivity;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


@RequiresApi(Build.VERSION_CODES.M)
public class FingerPrintAuthentication {
    private FingerprintManager fingerprintManager = null;
    private KeyguardManager keyguardManager = null;
    private KeyStore keyStore = null;
    private KeyGenerator keyGenerator = null;
    private String KEY_NAME = "fingerprint_key";
    private Cipher cipher = null;
    private FingerprintManager.CryptoObject cryptoObject = null;
    private OnScanFingerPrintInterface onScanFingerprintlistener;
    private AlertDialog.Builder dialogBuilder = null;
    private ProgressDialog progressDialog = null;
    private Context context;

    public FingerPrintAuthentication(Context context) {
        this.context = context;
    }

    public void managerFingerPrint(final OnScanFingerPrintInterface listener, ProgressDialog progressDialog) {
        this.onScanFingerprintlistener = listener;
        //this.progressDialog = progressDialog;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Toast.makeText(context, "Please touch your finger on fingerprint scanner for authentaction", Toast.LENGTH_SHORT).show()

            if (getManagers()) {
                Thread thread = new Thread(new Runnable() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void run() {
                        generateKey();

                        if (cipherInit()) {
                            cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        }

                        FingerprintHandler fingerprintHandler = new FingerprintHandler(context, onScanFingerprintlistener);
                        fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                    }
                });
                thread.start();
                //progressDialog.dismiss();
            }
        } else {
            //progressDialog.dismiss();
            //TODO("VERSION.SDK_INT < M")
        }
    }

    private Boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        }/* catch (@SuppressLint("NewApi") KeyPermanentlyInvalidatedException e){
            return false;
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (CertificateException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }*/ catch (Exception e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT |
                    KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } /*catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Failed to get KeyGenerator instance", e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/ catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("LongLogTag")
    private Boolean getManagers() {
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

        //progressDialog?.dismiss()

        if (keyguardManager.isKeyguardSecure() == false) {
            //Log.e("FingerPrintAuthentication", "isKeyguardSecure");

            dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage("Lock screen security not enabled in Settings.")
                    .setCancelable(false);

            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK || Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Constants.IS_DIALOG_DISMISS = true;
                    dialog.cancel();
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    context.startActivity(intent);
                }
            });

            AlertDialog alert = dialogBuilder.create();
            alert.setTitle("Fingerprint Authentication");
            if (!alert.isShowing()) {
                alert.show();
            }
            return false;

        } else if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            //Log.e("FingerPrintAuthentication", "USE_FINGERPRINT");

            dialogBuilder.setMessage("Fingerprint authentication permission not enabled.")
                    .setCancelable(false);

            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK || Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Constants.IS_DIALOG_DISMISS = true;
                    dialog.cancel();
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    context.startActivity(intent);
                }
            });

            AlertDialog alert = dialogBuilder.create();
            alert.setTitle("Fingerprint Authentication");
            if (!alert.isShowing()) {
                alert.show();
            }
            return false;

        } else if (fingerprintManager.hasEnrolledFingerprints() == false) {
            //Log.e("FingerPrintAuthentication", "hasEnrolledFingerprints");

            dialogBuilder.setMessage("You have not registered any Fingerprint yet.")
                    .setCancelable(false);

            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK || Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Constants.IS_DIALOG_DISMISS = true;
                    dialog.cancel();
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    context.startActivity(intent);
                }
            });

            AlertDialog alert = dialogBuilder.create();
            alert.setTitle("Fingerprint Authentication");
            if (!alert.isShowing()) {
                alert.show();
            }
            return false;

        } else {
            //Log.e("FingerPrintAuthentication", "else");

            Constants.IS_DIALOG_DISMISS = true;
            return true;
        }
    }
}
