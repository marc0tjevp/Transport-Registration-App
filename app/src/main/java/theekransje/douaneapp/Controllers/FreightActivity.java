package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import android.text.InputFilter;
import android.view.KeyEvent;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import theekransje.douaneapp.API.LocationAPI;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.R;

public class FreightActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = FreightActivity.class.getSimpleName();

    private static final int SCAN_BARCODE = 1;
    private FreightAdapter freightAdapter;

    private ArrayList<Freight> freights;
    private Driver driver;
    private Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight);

        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");

        BottomNavigationView navigation = this.findViewById(R.id.freight_navbar);
        navigation.setSelectedItemId(R.id.navbar_freight);
        navigation.setOnNavigationItemSelectedListener(this);


        ListView listview = findViewById(R.id.list_view);
        List<String> s = new ArrayList<>();
        freightAdapter = new FreightAdapter(s, this.getLayoutInflater());
        listview.setAdapter(freightAdapter);
        freightAdapter.notifyDataSetChanged();
        EditText editText = findViewById(R.id.mrnEditText);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

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
                    Object response = null;
                    try {
                        response = new LocationAPI().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (response != null) {
                        JSONObject jsonObject;
                        try {
                            // Top level json object
                            jsonObject = new JSONObject((String) response);


                            String country = jsonObject.getString("country");

                            Log.i(TAG, "Country = " + country);
                            if (country != null && country.equals("Netherlands")) {
                                String mrnCode = data.getData().toString();
                                freightAdapter.addMRN(mrnCode);
                                freightAdapter.notifyDataSetChanged();
                            } else {
                                String message = getResources().getString(R.string.country_not_netherlands);

                                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } catch (JSONException ex) {
                            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
                        }

                    }
                }
            }
        }
    }

    public void addMRN(View view) {
        Object response = null;
        try {
            response = new LocationAPI().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (response != null) {
            JSONObject jsonObject;
            try {
                // Top level json object
                jsonObject = new JSONObject((String) response);


                String country = jsonObject.getString("country");

                Log.i(TAG, "Country = " + country);
                if (country != null && country.equals("Netherlands")) {
                    EditText editText = findViewById(R.id.mrnEditText);
                    String mrnCode = editText.getText().toString();
                    if (mrnCode.matches("[0-9]{2}[A-Z]{2}[0-9A-Z]{14}")) {
                        freightAdapter.addMRN(mrnCode);
                        freightAdapter.notifyDataSetChanged();
                        editText.setText("");
                    } else {
                        String message = getResources().getString(R.string.code_not_mrn);

                        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    String message = getResources().getString(R.string.country_not_netherlands);

                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException ex) {
                Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
            }

        }
    }

    public void sendCodes(View view) {
        Toast.makeText(this, R.string.sending_codes, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navbar_status:
                Navbar.goToStatus(c, driver, freights);

                return true;
            case R.id.navbar_freight:
                Log.d(TAG, "onNavigationItemSelected: FIRED");
                Navbar.goToFreights(c, driver, freights);
                return true;
            case R.id.navbar_drive:
                Navbar.goToDrive(c, driver, freights);

                return true;
        }
        return false;
    }
}
