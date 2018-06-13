package theekransje.douaneapp.Controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import theekransje.douaneapp.API.AsyncLogin;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Interfaces.OnLoginResult;
import theekransje.douaneapp.Persistence.BackgroundDataSenderThread;
import theekransje.douaneapp.R;

public class LoginActivity extends AppCompatActivity implements OnLoginResult {
    private static final String TAG = "LoginActivity";
    public static boolean serviceIsRunning = false;

    int hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        serviceIsRunning = false;
        final TextView userName = (TextView) findViewById(R.id.login_user);
        final TextView passWd = (TextView) findViewById(R.id.login_passwd);
        final TextView IMEIView = (TextView) findViewById(R.id.login_imei);

        ///////set IMEI

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            try {
                String IMEI = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

                this.hash = IMEI != null? IMEI.hashCode():"dskldasj".hashCode();

                if (hash < 0) {
                    hash = hash * (-1);
                }

                IMEIView.setText("Unique ID: " + hash);

            } catch (SecurityException e) {
                e.printStackTrace();
                Log.d(TAG, "onCreate: MISSING PERMISSIONS FOR SECURITY CHECK");
            }
        }
        //asks for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2873);
            return;
        }


        ((Button) findViewById(R.id.login_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!passWd.getText().equals("")
                        &&
                        !userName.getText().equals("")
                        &&
                        !passWd.getText().equals(R.string.prompt_passwd)
                        &&
                        !userName.getText().equals(R.string.prompt_email)
                        ) {
                    Driver driver = new Driver();
                    driver.setUserName(userName.getText().toString());
                    driver.setPasswd(passWd.getText().toString());
                    driver.setIMEIHash(hash+"");

                    new AsyncLogin(driver, (OnLoginResult) view.getContext()).execute();

                }
                ;

            }
        });

        createNotificationChannel();
    }

    @Override
    public void onLoginSucces(Driver driver) {

        if (BackgroundDataSenderThread.thread != null){
            BackgroundDataSenderThread.thread.endThread();
            BackgroundDataSenderThread.thread = null;
        }

        BackgroundDataSenderThread.thread = new BackgroundDataSenderThread(this);
        BackgroundDataSenderThread.thread.start();

        Log.d(TAG, "onLoginSucces: called");
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra("DRIVER", driver);
        intent.putExtra("FREIGHTS",new ArrayList<Freight>());

        startActivity(intent);
    }

    @Override

    public void onLoginFailure(@StringRes final int error ) {
        Log.d(TAG, "onLoginFailure: called");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, getString(error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Douanen app";
            String description = "A channel dedicated to the Douanen app";

            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel("DOEANENAPPTRANSIT", name, NotificationManager.IMPORTANCE_MAX);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
