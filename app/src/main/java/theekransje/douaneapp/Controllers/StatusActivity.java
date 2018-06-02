package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import theekransje.douaneapp.API.AsyncGetStatusUpdate;
import theekransje.douaneapp.Domain.DouaneStatus;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Domain.MRMFormulier;
import theekransje.douaneapp.Interfaces.OnStatusUpdate;
import theekransje.douaneapp.R;

public class StatusActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnStatusUpdate {

    private ArrayList<Freight> freights;
    private Driver driver;
    private Context c;


    private static final String TAG = "StatusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");

        BottomNavigationView navigation = this.findViewById(R.id.status_navbar);
        navigation.setSelectedItemId(R.id.navbar_status);
        navigation.setOnNavigationItemSelectedListener(this);


        final ArrayList<Freight> data = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            MRMFormulier mrn = new MRMFormulier();
            mrn.setAfzender("Bob");
            mrn.setOntvanger("Eric");
            mrn.setMrn("" + (new Random().nextInt(100000)) + 1010000);
            Freight freight = new Freight();
            freight.setDouaneStatus(DouaneStatus.values()[new Random().nextInt(DouaneStatus.values().length - 1)]);
            freight.setMrmFormulier(mrn);
            data.add(freight);
        }

        RecyclerView rv = findViewById(R.id.status_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        rv.scrollToPosition(0);
        StatusAdapter adapter = new StatusAdapter(data, this);

        rv.setAdapter(adapter);

      //  new StatusTimer(this);
    }

    @Override
    public void onStatusUpdateAvail(Freight freights) {
        for (Freight freight : this.freights){
            if (freight.getMrmFormulier().Mrn.equals(freights.getMrmFormulier().Mrn)&&!freight.getMrmFormulier().equals(freights.getMrmFormulier())){
                Toast.makeText(this,freights.getMrmFormulier().Mrn+" has an update",Toast.LENGTH_SHORT).show();
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
