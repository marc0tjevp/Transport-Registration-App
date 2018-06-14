package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        ((TextView) (findViewById(R.id.status_detail_client))).setText("" + f.getMRNFormulier().getClient());
        ((TextView) (findViewById(R.id.status_detail_datetime))).setText("" + f.getMRNFormulier().getDateTime() + "");
        ((TextView) (findViewById(R.id.status_detail_recipient))).setText("" + f.getMRNFormulier().getReceiver());
        ((TextView) (findViewById(R.id.status_detail_reference))).setText("" + f.getMRNFormulier().getReference());
        ((TextView) (findViewById(R.id.status_detail_item_amount))).setText("" + f.getMRNFormulier().getArticleAmount());
        ((TextView) (findViewById(R.id.status_detail_sender))).setText("" + f.getMRNFormulier().getSender());
        ((TextView) (findViewById(R.id.status_detail_mrn))).setText("MRN: " + f.getMRNFormulier().getMrn());
        ((TextView) (findViewById(R.id.status_detail_total_weight))).setText("" + f.getMRNFormulier().getTotalWeight() + "");
        ((TextView) (findViewById(R.id.status_detail_status))).setText("Status:  " + f.getDouaneStatus().toString());
        ((TextView) (findViewById(R.id.status_detail_value))).setText("" + f.getMRNFormulier().getTotalPRice() + " " + f.getMRNFormulier().getCurrency());
        ((TextView) (findViewById(R.id.status_detail_origin))).setText("" + f.getMRNFormulier().getOrigin());
        ((TextView) (findViewById(R.id.status_detail_destination))).setText("" + f.getMRNFormulier().getDestination());
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
