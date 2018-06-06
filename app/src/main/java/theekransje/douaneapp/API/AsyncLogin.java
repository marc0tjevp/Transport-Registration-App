package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;

import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Interfaces.OnLoginResult;

/**
 * Created by Sander on 5/24/2018.
 */

//Inloggen en resultaat terugkoppelen op interface. Bij positief resultaat moet een token worden toegekend!


public class AsyncLogin extends AsyncTask {
    private static final String TAG = "AsyncLogin";
    private final String endPoint = "auth/login";
    private OnLoginResult listener;

    private Driver driver;

    public AsyncLogin(Driver driver, OnLoginResult onLoginResult) {
        this.listener = onLoginResult;
        this.driver = driver;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        ApiHelper apiHelper = new ApiHelper(endPoint, APIMethodes.POST);
        try {
            HttpURLConnection conn = apiHelper.getConnection();


            JSONObject j = new JSONObject();
            j.put("username", driver.getUserName());
            j.put("password", driver.getPasswd());
            j.put("imei", driver.getIMEIHash());

            Log.d(TAG, "doInBackground: logging in user: " + driver.getUserName()+ "   "+ driver.getPasswd() +" "+ driver.getIMEIHash());

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());

            os.write(j.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int statusCode = conn.getResponseCode();
            Log.d(TAG, "doInBackground: statuscode: "+ statusCode);

            if(statusCode == 200){
                JSONObject r = new JSONObject(ApiHelper.convertIStoString(conn.getInputStream()));
                driver.setToken(r.getString("token"));
                listener.onLoginSucces(driver);
            }else if(statusCode == 401) {
                Log.d(TAG, "doInBackground: Login failed");;

                listener.onLoginFailure( "invalid credentials");
            }else{
                listener.onLoginFailure("undefined error");
            }

            conn.disconnect();
            conn = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
