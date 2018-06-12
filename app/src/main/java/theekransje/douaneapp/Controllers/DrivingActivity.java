package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import theekransje.douaneapp.API.AsyncSendTime;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Domain.TimerClock;
import theekransje.douaneapp.Interfaces.OnTimeChange;
import theekransje.douaneapp.R;

public class DrivingActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,OnTimeChange{
    private static final String TAG = "DrivingActivity";

    private ArrayList<Freight> freights;
    private Driver driver;
    private Context c;
    private DrivingState state;
    private TextView view;
    private Button drivingButton;
    private ToggleButton pauseButton;
    private TimerClock drivenTime;
    private TimerClock realTime;
    private String text = "00:00:00";
    private Handler handler;
    private OnTimeChange listener = this;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);
        //variable definition
        this.handler = new Handler();
        this.state = DrivingState.Stopped;
        this.c = this;
        this.driver = (Driver) getIntent().getSerializableExtra("DRIVER");
        this.freights = (ArrayList<Freight>) getIntent().getSerializableExtra("FREIGHTS");
        this.drivingButton = findViewById(R.id.driving_end_button);
        this.pauseButton = findViewById(R.id.driving_break_button);
        this.view = findViewById(R.id.driving_time_view);

        view.setText(text);
        //Setting onClickListener for Starting/Stopping the drive
        drivingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //schedules the Timer's tasks
                if (drivingButton.getText().equals(getString(R.string.start_of_drive))) {
                    drivenTime=new TimerClock(handler,listener);
                    realTime = new TimerClock(handler);
                    handler.postDelayed(drivenTime,1000);
                    handler.postDelayed(realTime,1000);
                    drivingButton.setText(R.string.end_of_drive);
                    state= DrivingState.Driving;
                } else if (drivingButton.getText().equals(getString(R.string.end_of_drive))){
                    state = DrivingState.Stopped;
                    handler.removeCallbacks(realTime);
                    handler.removeCallbacks(drivenTime);
                    drivingButton.setText(R.string.start_of_drive);
                    Object[] data = {drivenTime.getDate(),state};
                    new AsyncSendTime().execute(data);
                }
            }});
        pauseButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (state==DrivingState.Stopped){
                    pauseButton.setChecked(false);
                }else {
                    if (isChecked){
                        Object[] data = {drivenTime.getDate(),state};
                        new AsyncSendTime().execute(data);
                        handler.removeCallbacks(drivenTime);
                        drivenTime=new TimerClock(handler,listener);
                        handler.postDelayed(drivenTime,1000);
                        state = DrivingState.Paused;
                    } else {
                        Object[] data = {drivenTime.getDate(),state};
                        new AsyncSendTime().execute(data);
                        handler.removeCallbacks(drivenTime);
                        drivenTime=new TimerClock(handler,listener);
                        handler.postDelayed(drivenTime,1000);
                        state = DrivingState.Driving;

                    }
                }
            }
        });



        BottomNavigationView navigation = this.findViewById(R.id.driving_navbar);
        navigation.setSelectedItemId(R.id.navbar_drive);
        navigation.setOnNavigationItemSelectedListener(this);


    }
    public void onTimeChange(String string){
        view.setText(string);
        this.text = string;
    }

    @Override
    public void toast() {
        Toast.makeText(this,R.string.offer_break,Toast.LENGTH_LONG).show();
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
