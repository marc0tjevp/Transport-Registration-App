package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import theekransje.douaneapp.API.AsyncGetFreights;
import theekransje.douaneapp.API.LocationAPI;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Interfaces.OnFreightListAvail;
import theekransje.douaneapp.R;
import theekransje.douaneapp.Util.ListViewAdapter;

public class FreightActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, OnFreightListAvail {
    private static final String TAG = FreightActivity.class.getSimpleName();

    private static final int SCAN_BARCODE = 1;
    private FreightAdapter freightAdapter;

    private ArrayList<Freight> freights;
    private ArrayList<String> allFreightMrn;
    private Driver driver;
    private Context c;

    private static ArrayList<String> selected = new ArrayList<>();

    ListView listview;
    ListView list;
    ListViewAdapter adapter;
    SearchView editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight);
        FreightActivity.selected = new ArrayList<>();


        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");


        if (this.freights != null && this.freights.size() > 0) {
            for (Freight f : freights) {
                FreightActivity.selected.add(f.getMRNFormulier().getMrn());
            }
        }



        BottomNavigationView navigation = this.findViewById(R.id.freight_navbar);
        navigation.setOnNavigationItemSelectedListener(this);


        listview = findViewById(R.id.status_list_view);


        final ArrayList<String> selected = new ArrayList<>();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                            if (FreightActivity.selected.contains(new String(allFreightMrn.get(position)))) {
                                view.setBackgroundColor(Color.WHITE);
                                FreightActivity.selected.remove(allFreightMrn.get(position));
                            } else {
                                view.setBackgroundColor(Color.GREEN);
                                FreightActivity.selected.add(allFreightMrn.get(position));
                                Log.d(TAG, "sendCodes: sending mrns: " + FreightActivity.selected.toString());

                                Log.d(TAG, "onItemClick: selected now contains entries: " + FreightActivity.selected.size());
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

        });


        list = (ListView)

                findViewById(R.id.listview);

        adapter = new

                ListViewAdapter(this, selected);
        list.setAdapter(adapter);
        editSearch = (SearchView)

                findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);

        onQueryTextChange("");
        list.setVisibility(View.GONE);


        new

                AsyncGetFreights(this, driver).

                execute();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(c, "Kan niet terug tijdens het rijden.", Toast.LENGTH_SHORT).show();
            }
        });

        navigation.setVisibility(View.INVISIBLE);
    }

    public void scan(View view) {

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
                    startActivityForResult(new Intent(this, BarcodeScannerActivity.class), SCAN_BARCODE);

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

    }

    public void sendCodes(View view) {
        Toast.makeText(this, R.string.sending_codes, Toast.LENGTH_LONG).show();

        Log.d(TAG, "goToStatus: Fired");

        Intent intent = new Intent(c, StatusActivity.class);
        intent.putExtra("DRIVER", driver);
        intent.putExtra("FREIGHTS", freights);

        Log.d(TAG, "sendCodes: sending mrns: " + FreightActivity.selected.toString());
        intent.putExtra("MRN", FreightActivity.selected);


        c.startActivity(intent);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navbar_status:
                Navbar.goToStatus(c, driver, freights);

                return true;

            case R.id.navbar_drive:
                Navbar.goToDrive(c, driver, freights);

                return true;
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navigation = this.findViewById(R.id.freight_navbar);
        navigation.setSelectedItemId(R.id.freight_navbar);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        String text = newText;
        adapter.filter(text);
        return false;
    }

    @Override
    public void OnFreightListAvail(ArrayList<String> freights) {
        this.allFreightMrn = freights;

        if (freights.size() == 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(c, "Geen vrachten gevonden.", Toast.LENGTH_SHORT).show();
                }
            });

        }
        freightAdapter = new FreightAdapter(freights, this.getLayoutInflater(), selected);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                listview.setAdapter(freightAdapter);
                freightAdapter.notifyDataSetChanged();
            }
        });


    }
}
