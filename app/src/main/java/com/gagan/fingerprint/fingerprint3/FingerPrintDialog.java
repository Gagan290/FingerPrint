package com.gagan.fingerprint.fingerprint3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gagan.fingerprint.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class FingerPrintDialog extends BottomSheetDialog implements View.OnClickListener {
    private Context context = null;
    private Button btnCancel = null;
    private ImageView imgLogo = null;
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

        btnCancel = findViewById(R.id.btn_cancel);
        imgLogo = findViewById(R.id.img_logo);
        itemTitle = findViewById(R.id.item_title);
        itemStatus = findViewById(R.id.item_status);
        itemSubtitle = findViewById(R.id.item_subtitle);
        itemDescription = findViewById(R.id.item_description);

        btnCancel.setOnClickListener(this);

        updateLogo();
    }

    public void setTitle(String title) {
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

    private void updateLogo() {
        try {
            Drawable drawable = getContext().getPackageManager().getApplicationIcon(context.getPackageName());
            imgLogo.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel) {
            scanFingerPrintInterface.onFingerPrintCancel();
        }
    }
}
