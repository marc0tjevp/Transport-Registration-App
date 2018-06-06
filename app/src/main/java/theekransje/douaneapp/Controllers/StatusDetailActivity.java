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
        navigation.setSelected(false);
        navigation.setOnNavigationItemSelectedListener(this);



            Freight f = (Freight) getIntent().getSerializableExtra("FREIGHT");

        ((TextView)(findViewById(R.id.status_detail_client))).setText(""+f.getMrmFormulier().Opdrachtgever);
        ((TextView)(findViewById(R.id.status_detail_datetime))).setText(""+f.getMrmFormulier().DateTime + "");
        ((TextView)(findViewById(R.id.status_detail_recipient))).setText(""+f.getMrmFormulier().Ontvanger);
        ((TextView)(findViewById(R.id.status_detail_reference))).setText(""+f.getMrmFormulier().getReference());
        ((TextView)(findViewById(R.id.status_detail_item_amount))).setText(""+f.getMrmFormulier().getAantalArtikelen());
        ((TextView)(findViewById(R.id.status_detail_sender))).setText(""+f.getMrmFormulier().Afzender);
        ((TextView)(findViewById(R.id.status_detail_mrn))).setText(         "MRN: "  + f.getMrmFormulier().Mrn);
        ((TextView)(findViewById(R.id.status_detail_total_weight))).setText(""+f.getMrmFormulier().TotaalGewicht + "");
        ((TextView)(findViewById(R.id.status_detail_status))).setText(      "Status:  " +f.getDouaneStatus().toString());
        ((TextView)(findViewById(R.id.status_detail_value))).setText(""+f.getMrmFormulier().TotaalBedrag + "" + f.getMrmFormulier().Currency);
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

}
