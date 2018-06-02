package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.R;

public class StatusDetailActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "StatusDetailActivity";


    private ArrayList<Freight> freights;
    private Driver driver;
    private Context c;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);


        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");


        BottomNavigationView navigation = this.findViewById(R.id.driving_navbar);
        navigation.setSelectedItemId(R.id.navbar_drive);
        navigation.setOnNavigationItemSelectedListener(this);



            Freight f = (Freight) getIntent().getSerializableExtra("FREIGHT");

        ((TextView)(findViewById(R.id.status_detail_client))).setText(      "Opdrachtgever:     "+f.getMrmFormulier().Opdrachtgever);
        ((TextView)(findViewById(R.id.status_detail_datetime))).setText(    "Datum:             "  + f.getMrmFormulier().DateTime + "");
        ((TextView)(findViewById(R.id.status_detail_recipient))).setText(   "Ontvanger:         "  + f.getMrmFormulier().Ontvanger);
        ((TextView)(findViewById(R.id.status_detail_reference))).setText(   "Referentie:        "  + f.getMrmFormulier().getReference());
        ((TextView)(findViewById(R.id.status_detail_item_amount))).setText( "Aantal artikelen   " + f.getMrmFormulier().getAantalArtikelen());
        ((TextView)(findViewById(R.id.status_detail_sender))).setText(      "Verzender:         "  + f.getMrmFormulier().Afzender);
        ((TextView)(findViewById(R.id.status_detail_mrn))).setText(         "MRN:               "  + f.getMrmFormulier().Mrn);
        ((TextView)(findViewById(R.id.status_detail_total_weight))).setText("Totaal gewicht:    "  + f.getMrmFormulier().TotaalGewicht + "");
        ((TextView)(findViewById(R.id.status_detail_status))).setText(      "Status:            " +f.getDouaneStatus().toString());
        ((TextView)(findViewById(R.id.status_detail_value))).setText(       "Waarde:            "+f.getMrmFormulier().TotaalBedrag + "" + f.getMrmFormulier().Currency);
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
