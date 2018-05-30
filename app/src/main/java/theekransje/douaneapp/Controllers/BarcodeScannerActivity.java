package theekransje.douaneapp.Controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import theekransje.douaneapp.R;

public class BarcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA_PERMISSION = 103;
    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                finish();
                Toast.makeText(this, getText(R.string.no_permission_camera), Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } else {
            zXingScannerView = new ZXingScannerView(this);
            setContentView(zXingScannerView);
            zXingScannerView.setResultHandler(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    zXingScannerView = new ZXingScannerView(this);
                    setContentView(zXingScannerView);
                    zXingScannerView.setResultHandler(this);
                } else {
                    finish();
                    Toast.makeText(this, getText(R.string.no_permission_camera), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (zXingScannerView != null) {
            zXingScannerView.setResultHandler(this);
            zXingScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (zXingScannerView != null) {
            zXingScannerView.stopCamera();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent data = new Intent();
        String text = rawResult.getText();
        Log.i("Barcodescanneractivity", text);
        if (text.matches("[0-9]{2}[A-Z]{2}[0-9A-Z]{14}")){
            data.setData(Uri.parse(text));
            setResult(RESULT_OK, data);
        } else {
            String message = getResources().getString(R.string.code_not_mrn);
            Toast  toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();

            zXingScannerView.setResultHandler(this);
        }

        finish();
    }
}
