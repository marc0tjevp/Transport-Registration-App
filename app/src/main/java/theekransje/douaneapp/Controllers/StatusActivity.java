package theekransje.douaneapp.Controllers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import theekransje.douaneapp.API.AsyncGetPDF;
import theekransje.douaneapp.API.AsyncGetStatusDetail;
import theekransje.douaneapp.API.LocationAPI;
import theekransje.douaneapp.Domain.DouaneStatus;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Domain.MRNFormulier;
import theekransje.douaneapp.Interfaces.OnPDFAvail;
import theekransje.douaneapp.Interfaces.OnStatusDetailAvail;
import theekransje.douaneapp.Interfaces.OnStatusUpdate;
import theekransje.douaneapp.R;

public class StatusActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnStatusUpdate, OnStatusDetailAvail, OnPDFAvail {

    private ArrayList<Freight> freights;
    private Driver driver;
    private Context c;
    private ArrayList<String> selectedMRN;
    private StatusAdapter adapter;
    private Thread t;
    private BottomNavigationView navigation;

    private static final String TAG = "StatusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");
        this.selectedMRN = (ArrayList<String>) getIntent().getSerializableExtra("MRN");


        navigation = this.findViewById(R.id.status_navbar);
        navigation.setSelectedItemId(R.id.navbar_status);
        navigation.setVisibility(View.GONE);


        if (this.freights == null || this.freights.size() == 0) {
            this.freights = new ArrayList<>();
        }
        if (this.selectedMRN == null) {
            Log.d(TAG, "onCreate: MRN list empty, creating empty array");
            this.selectedMRN = new ArrayList<>();
        } else {
            Log.d(TAG, "onCreate: SELECTED MRNs" + this.selectedMRN.size());
        }

        for (String s : this.selectedMRN) {
            new AsyncGetStatusDetail(s, this).execute();
        }


        RecyclerView rv = findViewById(R.id.status_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        rv.scrollToPosition(0);
        adapter = new StatusAdapter(this.freights, this, this.driver);

        rv.setAdapter(adapter);


        ImageButton imageButton = findViewById(R.id.status_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            Intent intent = new Intent(v.getContext(), FreightActivity.class);
                            v.getContext().startActivity(intent);
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

        this.t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    boolean allready = false;

                    while (!allready) {
                        allready = true;
                        if (adapter.getmData().size() == 0){
                            break;
                        }

                        Log.d(TAG, "run: Status Update thread running");
                        for (Freight f : adapter.getmData()
                                ) {

                            if (!f.getDouaneStatus().equals(DouaneStatus.VERTREK_OK)) {
                                Log.d(TAG, "run: status doesnt eq. VERTREK_OK updating form " + f.getMRNFormulier().getMrn());
                                updateStatus(f.getMRNFormulier().getMrn());
                                allready = false;
                            } else {

                            }
                            if (!f.isPdfAvail() && f.getDouaneStatus().equals(DouaneStatus.VERTREK_OK)) {
                                allready = false;
                                new AsyncGetPDF(f.getMRNFormulier().getMrn(), (OnPDFAvail) c, (AppCompatActivity) c).execute();
                            }
                            Log.d(TAG, "run: " + f.isPdfAvail() + f.getDouaneStatus());
                            Log.d(TAG, "run: size" + adapter.getmData().size());
                            Log.d(TAG, "run: " + adapter.getmData().toString());

                        }
                            if (allready) {
                                Log.d(TAG, "run: Thread shutdown as planned");
                                navigation.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) c);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        navigation.setVisibility(View.VISIBLE);
                                    }
                                });

                                break;


                        }


                        Log.d(TAG, "run: sleeping for 5s");
                        Thread.sleep(1000);
                    }


                } catch (Exception e) {

                }
            }
        };

        t.start();

        //    new StatusTimer(this);
    }

    @Override
    public void onStatusUpdateAvail(Freight freights) {
        for (Freight freight : this.freights) {
            if (freight.getMRNFormulier().Mrn.equals(freights.getMRNFormulier().Mrn) && !freight.equals(freights)) {
                Toast.makeText(this, freights.getMRNFormulier().Mrn + " has an update", Toast.LENGTH_SHORT).show();
                this.freights.remove(freight);
                this.freights.add(freights);
            }
        }
    }

    @Override
    public ArrayList<Freight> getFreights() {
        return freights;
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
        if (t.isInterrupted()){
            t.start();
        }


    }

    @Override
    public void OnStatusDetailAvail(final Freight freight) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addFreight(freight);
            }
        });
    }

    public void updateStatus(String mrn) {
        Log.d(TAG, "updateStatus: for " + mrn);
        new AsyncGetStatusDetail(mrn, this).execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: killing update thread");
        t.interrupt();
        ;
    }


    @Override
    public void OnPDFAvail(final String s, String mrn) {
        for (final Freight f : adapter.getmData()) {
            if (f.getMRNFormulier().getMrn().equals(mrn)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        f.setPdf(s);
                        f.setPdfAvail(true);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
    @Override
    public void onBackPressed() {

    }
}
