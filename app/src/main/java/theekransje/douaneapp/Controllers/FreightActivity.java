package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.R;
import theekransje.douaneapp.Util.ListViewAdapter;

public class FreightActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private static final String TAG = FreightActivity.class.getSimpleName();

    private static final int SCAN_BARCODE = 1;
    private FreightAdapter freightAdapter;

    private ArrayList<Freight> freights;
    private Driver driver;
    private Context c;

    private List<String> s;


    ListView list;
    ListViewAdapter adapter;
    SearchView editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight);

        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");

        BottomNavigationView navigation = this.findViewById(R.id.freight_navbar);
        navigation.setOnNavigationItemSelectedListener(this);


        ListView listview = findViewById(R.id.status_list_view);
        s = new ArrayList<>();


        final List<String> selected = new ArrayList<>();

        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));
        s.add("14DB" + new Random().nextInt(1000000000));



                freightAdapter = new FreightAdapter(s, this.getLayoutInflater());
        listview.setAdapter(freightAdapter);
        freightAdapter.notifyDataSetChanged();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (selected.contains(new String(s.get(position)))) {
                    view.setBackgroundColor(Color.WHITE);
                    selected.remove(s.get(position));
                } else {
                    view.setBackgroundColor(Color.GREEN);
                    selected.add(s.get(position));
                }

            }

        });



        list = (ListView) findViewById(R.id.listview);

        adapter = new ListViewAdapter(this, s);


        list.setAdapter(adapter);

        editSearch = (SearchView) findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);

        onQueryTextChange("123");

        list.setVisibility(View.GONE);
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


}
