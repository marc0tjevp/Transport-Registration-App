package theekransje.douaneapp.Controllers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.InputStreamReader;
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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");


        BottomNavigationView navigation = this.findViewById(R.id.driving_navbar);
        navigation.setSelected(false);
        navigation.setOnNavigationItemSelectedListener(this);



           final Freight f = (Freight) getIntent().getSerializableExtra("FREIGHT");

        ((TextView)(findViewById(R.id.status_detail_client))).setText(""+f.getMRNFormulier().Opdrachtgever);
        ((TextView)(findViewById(R.id.status_detail_datetime))).setText(""+f.getMRNFormulier().DateTime + "");
        ((TextView)(findViewById(R.id.status_detail_recipient))).setText(""+f.getMRNFormulier().Ontvanger);
        ((TextView)(findViewById(R.id.status_detail_reference))).setText(""+f.getMRNFormulier().getReference());
        ((TextView)(findViewById(R.id.status_detail_item_amount))).setText(""+f.getMRNFormulier().getAantalArtikelen());
        ((TextView)(findViewById(R.id.status_detail_sender))).setText(""+f.getMRNFormulier().Afzender);
        ((TextView)(findViewById(R.id.status_detail_mrn))).setText(         "MRN: "  + f.getMRNFormulier().Mrn);
        ((TextView)(findViewById(R.id.status_detail_total_weight))).setText(""+f.getMRNFormulier().TotaalGewicht + "");
        ((TextView)(findViewById(R.id.status_detail_status))).setText(      "Status:  " +f.getDouaneStatus().toString());
        ((TextView)(findViewById(R.id.status_detail_value))).setText(""+f.getMRNFormulier().TotaalBedrag + "" + f.getMRNFormulier().Currency);
        ((TextView) (findViewById(R.id.status_detail_recipient_adress))).setText(f.getMRNFormulier().getOntvangstAdres());
        ((TextView) (findViewById(R.id.status_detail_sender_adress))).setText(f.getMRNFormulier().getVerzenderAdres());



        ImageButton b = ((ImageButton) (findViewById(R.id.pdfAvail)));

        if(f.isPdfAvail()){
           b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{

                        String root = Environment.getExternalStorageDirectory().toString();
                        File target = new File(root+"/douane/"+f.getMRNFormulier().getMrn()+".pdf");
                        if (target.exists())
                        {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.fromFile(target);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            try
                            {
                                startActivity(intent);
                            }
                            catch(ActivityNotFoundException e)
                            {
                                Toast.makeText(c, "No Application available to view pdf", Toast.LENGTH_LONG).show();
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                }
                }
            });
        }else {
            b.setVisibility(View.INVISIBLE);
        }


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
