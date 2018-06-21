package theekransje.douaneapp.Controllers;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import theekransje.douaneapp.API.APIMethodes;
import theekransje.douaneapp.API.AsyncGetDrivenTimes;
import theekransje.douaneapp.Domain.APITask;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Interfaces.OnTimesReady;
import theekransje.douaneapp.Persistence.DBHelper;
import theekransje.douaneapp.R;



public class DrivingActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,OnTimesReady{

    private static final String TAG = "DrivingActivity";

    private ArrayList<Freight> freights;
    private Driver driver;
    private DrivingAdapter adapter;
    private Context c;
    private TextView timeTextView;
    private Button drivingButton;
    private DrivingState state;
    private Button pauseButton;
    private long startTime;
    private long endTime;
    private long realStartTime;
    private Thread t;
    private Thread t2;
    private boolean isPauze = false;
    private boolean isDriving = false;
    private BottomNavigationView navigation;
    private ArrayList<String> dates;

    private LocationService locationService = new LocationService();
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocationService.LocationTrackingBinder binder = (LocationService.LocationTrackingBinder) service;
            locationService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else {

        }


        //variable definition
        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");
        navigation = this.findViewById(R.id.driving_navbar);
        navigation.setSelectedItemId(R.id.navbar_drive);
        navigation.setOnNavigationItemSelectedListener(this);
        dates = new ArrayList<>();
        timeTextView = findViewById(R.id.driving_time_view);
        timeTextView.setText("00:00:00");
        drivingButton = findViewById(R.id.driving_end_button);
        pauseButton = findViewById(R.id.driving_break_button);
        adapter = new DrivingAdapter(dates,this.getLayoutInflater());
        ListView timeView= findViewById(R.id.time_list);
        timeView.setAdapter(adapter);


        if (freights.size() > 0) {
            Intent intent = new Intent(this, LocationService.class);
            intent.putExtra("mrn", freights.get(0).getMRNFormulier().Mrn);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }


        t = new Thread(){
            @Override
            public void run() {
                super.run();
            //    timeTextView.setText(System.currentTimeMillis()+"");
                if (isDriving){
                    navigation.setVisibility(View.INVISIBLE);
                    drivingButton.setText(R.string.end_of_drive);
                    startTime=System.currentTimeMillis();
                    realStartTime=System.currentTimeMillis();
                    state = DrivingState.Driving;

                    if (freights.size() > 0) {
                        Intent intent = new Intent(DrivingActivity.this, LocationService.class);
                        intent.putExtra("mrn", freights.get(0).getMRNFormulier().Mrn);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                        } else {
                            startService(intent);
                        }
                        locationService.startLocationTracking();


                } else if (drivingButton.getText().equals(getString(R.string.end_of_drive))) {

                    locationService.stopLocationTracking();
                }

                }else {
                        state = DrivingState.Stopped;
                        navigation.setVisibility(View.VISIBLE);
                        drivingButton.setText(R.string.start_of_drive);
                        endTime = System.currentTimeMillis();
                        Log.d(TAG, "Totaal gereden tijd: " + (endTime - startTime));
                        for (Freight freight : freights) {
                            Object[] data = {new Date(startTime).toString(), new Date(endTime).toString(), "Einde rit", freight.getMRNFormulier().getMrn()};
                            JSONObject object = new JSONObject();
                            try {
                                object.put("x", "x");
                            } catch (Exception e){
                                    e.printStackTrace();
                            }
                            String endpoint = "customs/status/"+freight.getMRNFormulier().getMrn();
                            new DBHelper(c).insertTask(new APITask(object,APIMethodes.PUT,endpoint));
                            sendTime(data);
                            timeTextView.setText("00:00:00");
                        }
                        Navbar.goToStatus(c,driver,new ArrayList<Freight>());
                }
                }
            };


        t2 = new Thread() {
            @Override
            public void run() {
                super.run();
                if (isPauze) {
                    pauseButton.setText(R.string.drive_resume);
                    endTime = System.currentTimeMillis();
                    Log.d(TAG, "Totaal gereden tijd: " + (endTime - startTime));
                    for (Freight freight : freights) {
                        Object[] data = {new Date(startTime).toString(), new Date(endTime).toString(), "Rijden", freight.getMRNFormulier().getMrn()};
                        sendTime(data);
                        getTimes();

                    }
                    adapter.addDate("Gereden   " + new Date(startTime).toString().split(" ")[3]+"   -   "+new Date(endTime).toString().split(" ")[3]);
                    adapter.notifyDataSetChanged();
                    startTime = System.currentTimeMillis();
                } else {

                        pauseButton.setText(R.string.drive_break);
                        endTime = System.currentTimeMillis();
                        Log.d(TAG, "Totaal gereden tijd: " + (endTime - startTime));
                        realStartTime = realStartTime + (endTime - startTime);
                        for (Freight freight : freights) {
                            Object[] data = {new Date(startTime).toString(), new Date(endTime).toString(), "Pauze", freight.getMRNFormulier().getMrn()};

                            sendTime(data);

                        }
                        startTime = System.currentTimeMillis();
                }
            }
        };
        Thread timeUpdateThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                while (true) {
                    while (!isPauze && isDriving) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Date date = new Date(System.currentTimeMillis() - realStartTime);
                                String[] dateString = date.toString().split(" ");
                                timeTextView.setText("" + dateString[3]);
                            }
                        });


                        Thread.sleep(100);
                    }


                    Thread.sleep(100);
                }

                }catch (Exception e){

                }}
        };

        drivingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isDriving){
                    if (isPauze) {
                        Toast.makeText(c, "Een rit eindigen is niet mogelijk gedurende een pauze", Toast.LENGTH_SHORT).show();
                    } else {
                        new AlertDialog.Builder(c)
                                .setTitle("Weet je zeker dat je klaar bent met rijden?")
                                .setMessage("Weet je zeker dat je klaar bent met rijden?")
                                .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isDriving = !isDriving;
                                runOnUiThread(t);
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
                        locationService.stopLocationTracking();
                    }
                    }else {
                        isDriving = !isDriving;
                        runOnUiThread(t);

                    }

            }});

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDriving){
                    isPauze = !isPauze;
                    runOnUiThread(t2);
                    getTimes();
                }else {
                    getTimes();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, "Eerst rijden, dan pauze", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
      timeUpdateThread.start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (!isDriving) {
        switch (item.getItemId()) {
            case R.id.navbar_status:

                    Navbar.goToStatus(c, driver, freights);

                    return true;

            case R.id.navbar_drive:
                Navbar.goToDrive(c, driver, freights);

                return true;
        }
    }  return false;}

    @Override
    public void onBackPressed() {
        if (isDriving) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(c, "Kan niet terug tijdens het rijden.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onTimesReady(final ArrayList<String> dates) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(String date:dates){
                    Log.d(TAG,date+"");
                    adapter.addDate(date);

                }adapter.notifyDataSetChanged();
            }
        });

    }

    private void sendTime(Object[] data){
        try {
            JSONObject object = new JSONObject();
            object.put("startTime", data[0]);
            object.put("endTime", data[1]);
            object.put("type", data[2]);
            object.put("mrn", data[3]);
            APITask task = new APITask(object, APIMethodes.POST, "drivetimes/senddrive");
            new DBHelper(c).insertTask(task);
        } catch (Exception e){

        }
    }
    public void getTimes(){
        new AsyncGetDrivenTimes(this).execute();
    }
}
