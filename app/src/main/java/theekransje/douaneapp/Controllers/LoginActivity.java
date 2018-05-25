package theekransje.douaneapp.Controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
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

import org.w3c.dom.Text;

import theekransje.douaneapp.API.AsyncLogin;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Interfaces.OnLoginResult;
import theekransje.douaneapp.R;

public class LoginActivity extends AppCompatActivity implements OnLoginResult {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView userName = (TextView) findViewById(R.id.login_user);
        final TextView passWd = (TextView) findViewById(R.id.login_passwd);
        TextView IMEIView = (TextView) findViewById(R.id.login_imei);


        ///////set IMEI

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            try {
                String IMEI = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

                int hash = IMEI.hashCode();

                if (hash < 0) {
                    hash = hash * (-1);
                }

                IMEIView.setText("Unique ID: " + hash);

            } catch (SecurityException e) {
                e.printStackTrace();
                Log.d(TAG, "onCreate: MISSING PERMISSIONS FOR SECURITY CHECK");
            }
        }


        ((Button) findViewById(R.id.login_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (    !passWd.getText().equals("")
                        &&
                        !userName.getText().equals("")
                        &&
                        !passWd.getText().equals(R.string.prompt_passwd)
                        &&
                        !userName.getText().equals(R.string.prompt_email)
                        ){
                    Driver driver = new Driver();
                    driver.setUserName(userName.getText().toString());
                    driver.setPasswd(passWd.getText().toString());

                    new AsyncLogin(driver,(OnLoginResult) view.getContext()).execute();

                };

            }
        });
    }

    @Override
    public void onLoginSucces(Driver driver) {
        Log.d(TAG, "onLoginSucces: called");
        Intent intent = new Intent(this, VrachtActivity.class);
        intent.putExtra("DRIVER", driver);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(final String error) {
        Log.d(TAG, "onLoginFailure: called");

         runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });



    }
}
