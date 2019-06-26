package com.jeeps.laboratorioutpl.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.jeeps.laboratorioutpl.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrScannerActivity extends AppCompatActivity {

    public static final String QR_CODE_RESULT = "QR_CODE_RESULT";
    @BindView(R.id.scanner_view) CodeScannerView scannerView;
    private CodeScanner codeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        ButterKnife.bind(this);

        scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);

        codeScanner.setDecodeCallback(result ->
                runOnUiThread(() -> {
                    // Start intent with QR code
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(QR_CODE_RESULT, result.getText());
                    // Clear activity stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }));
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        } else
            codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}
