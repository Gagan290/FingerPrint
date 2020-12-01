package com.gagan.fingerprint.fingerprint3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gagan.fingerprint.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class FingerPrintDialog extends BottomSheetDialog implements View.OnClickListener {
    private Context context = null;
    private LinearLayout linLogin = null;
    private ImageView imgFingerprint;
    private TextView itemTitle, itemDescription, itemSubtitle, itemStatus;
    private OnScanFingerPrintInterface scanFingerPrintInterface;

    public FingerPrintDialog(@NonNull Context context, OnScanFingerPrintInterface scanFingerPrintInterface) {
        super(context);
        this.context = context;
        this.scanFingerPrintInterface = scanFingerPrintInterface;

        setDialogView();
    }

    private void setDialogView() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        setContentView(bottomSheetView);

        imgFingerprint = findViewById(R.id.imgFingerprint);
        linLogin = findViewById(R.id.linLogin);

        linLogin.setOnClickListener(this);

        updateLogo("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linLogin) {
            scanFingerPrintInterface.onNormalLogin();
        }
    }

    public void updateLogo(String status) {
        try {
            Drawable drawable = null;
            if (status.equalsIgnoreCase("success")) {
                //Drawable drawable = getContext().getPackageManager().getApplicationIcon(context.getPackageName());
                drawable = context.getResources().getDrawable(R.drawable.ic_fingerprint_blue);

            } else if (status.equalsIgnoreCase("failed")) {
                drawable = context.getResources().getDrawable(R.drawable.ic_fingerprint_red);
            } else {
                drawable = context.getResources().getDrawable(R.drawable.ic_fingerprint_grey);
            }
            imgFingerprint.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public void setTitle(String title) {
        itemTitle.setText(title);
    }

    public void updateStatus(String status) {
        itemStatus.setText(status);
    }

    public void setSubtitle(String subtitle) {
        itemSubtitle.setText(subtitle);
    }

    public void setDescription(String description) {
        itemDescription.setText(description);
    }

    public void setButtonText(String negativeButtonText) {
        btnCancel.setText(negativeButtonText);
    }

    public void updateLogo(String status) {
        try {
            Drawable drawable = getContext().getPackageManager().getApplicationIcon(context.getPackageName());
            imgFingerprint.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}
