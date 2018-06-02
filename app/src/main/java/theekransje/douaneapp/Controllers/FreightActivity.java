package theekransje.douaneapp.Controllers;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import theekransje.douaneapp.R;

public class FreightActivity extends AppCompatActivity {
    private static final int SCAN_BARCODE = 1;
    private FreightAdapter freightAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight);
        ListView listview = findViewById(R.id.list_view);
        List<String> s = new ArrayList<>();
        freightAdapter = new FreightAdapter(s, this.getLayoutInflater());
        listview.setAdapter(freightAdapter);
        freightAdapter.notifyDataSetChanged();
        EditText editText = findViewById(R.id.mrnEditText);
        editText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    addMRN(null);
                    return true;
                }
                return false;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                        R.string.want_to_delete, Snackbar.LENGTH_LONG);
                mySnackbar.setAction(R.string.delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        freightAdapter.removeMRN(position);
                        freightAdapter.notifyDataSetChanged();
                    }
                });
                mySnackbar.show();
            }
        });
    }

    public void scan(View view) {
        startActivityForResult(new Intent(this, BarcodeScannerActivity.class), SCAN_BARCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_BARCODE: {
                if (resultCode == RESULT_OK) {
                    String mrnCode = data.getData().toString();
                    freightAdapter.addMRN(mrnCode);
                    freightAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void addMRN(View view) {
        EditText editText = findViewById(R.id.mrnEditText);
        String mrnCode = editText.getText().toString();
        if (mrnCode.matches("[0-9]{2}[A-Z]{2}[0-9A-Z]{14}")){
            freightAdapter.addMRN(mrnCode);
            freightAdapter.notifyDataSetChanged();
            editText.setText("");
        } else {
            String message = getResources().getString(R.string.code_not_mrn);

            Toast  toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void sendCodes(View view) {
        Toast.makeText(this, R.string.sending_codes, Toast.LENGTH_LONG).show();
    }
}
