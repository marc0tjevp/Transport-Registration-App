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

import java.util.ArrayList;

import theekransje.douaneapp.API.AsyncGetPDF;
import theekransje.douaneapp.API.AsyncGetStatusDetail;
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

    private static final String TAG = "StatusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");
        this.selectedMRN = (ArrayList<String>) getIntent().getSerializableExtra("MRN");


        BottomNavigationView navigation = this.findViewById(R.id.status_navbar);
        navigation.setSelectedItemId(R.id.navbar_status);
        navigation.setOnNavigationItemSelectedListener(this);

        if (this.freights == null || this.freights.size() == 0) {
            this.freights = new ArrayList<>();
        }
        if (this.selectedMRN == null) {
            Log.d(TAG, "onCreate: MRN list empty, creating empty array");
            this.selectedMRN = new ArrayList<>();
        } else {
            Log.d(TAG, "onCreate: SELECTED MRNs" + this.selectedMRN.size());
        }

        for (String s : this.selectedMRN
                ) {
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
                Intent intent = new Intent(v.getContext(), FreightActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        this.t = new Thread() {
            @Override
            public void run() {
                try {
                    boolean allready = false;

                    while (!allready) {

                        Log.d(TAG, "run: Status Update thread running");
                        for (Freight f : adapter.getmData()
                                ) {
                            allready = true;
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
                            if (allready){
                                Log.d(TAG, "run: Thread shutdown as planned");
                                break;
                            }

                        }
                        Log.d(TAG, "run: sleeping for 5s");
                        Thread.sleep(5000);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
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
        t.interrupt();;
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
}
