package theekransje.douaneapp.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import theekransje.douaneapp.R;

public class FreightActivity extends AppCompatActivity {
    private static final int SCAN_BARCODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight);
    }

    public void scan(View view) {
        startActivityForResult(new Intent(this, BarcodeScannerActivity.class), SCAN_BARCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SCAN_BARCODE:{
                if (requestCode == RESULT_OK){

                }
            }
        }
    }
}
