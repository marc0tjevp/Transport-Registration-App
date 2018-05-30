package theekransje.douaneapp.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.R;

public class StatusDetailActivity extends AppCompatActivity {

    private static final String TAG = "StatusDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);


            Freight f = (Freight) getIntent().getSerializableExtra("FREIGHT");

        ((TextView)(findViewById(R.id.status_detail_client))).setText(f.getMrmFormulier().Opdrachtgever);
        ((TextView)(findViewById(R.id.status_detail_currency))).setText(f.getMrmFormulier().Currency);
        ((TextView)(findViewById(R.id.status_detail_datetime))).setText(f.getMrmFormulier().DateTime + "");
        ((TextView)(findViewById(R.id.status_detail_item_amount))).setText(f.getMrmFormulier().AantalArtikelen);
        ((TextView)(findViewById(R.id.status_detail_recipient))).setText(f.getMrmFormulier().Ontvanger);
        ((TextView)(findViewById(R.id.status_detail_sender))).setText(f.getMrmFormulier().Afzender);
        ((TextView)(findViewById(R.id.status_detail_mrn))).setText(f.getMrmFormulier().Mrn);
        ((TextView)(findViewById(R.id.status_detail_total_weight))).setText(f.getMrmFormulier().TotaalGewicht + "");
        ((TextView)(findViewById(R.id.status_detail_client))).setText(f.getMrmFormulier().Opdrachtgever);
        ((TextView)(findViewById(R.id.status_detail_status))).setText(f.getDouaneStatus().toString());
        ((TextView)(findViewById(R.id.status_detail_value))).setText(f.getMrmFormulier().TotaalBedrag + "");
    }
}
