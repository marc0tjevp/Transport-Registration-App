package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.util.Base64Utils;

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
                ApiHelper.token = driver.getToken();
                Log.d(TAG, "doInBackground: token " + driver.getToken());

                String encodedString = driver.getToken().split("\\.")[1];
                Log.d(TAG, "doInBackground: " + encodedString);
                byte[] decodedByteArray = Base64Utils.decode(encodedString);
                String decodedToken = new String(decodedByteArray);


                Log.d(TAG, "doInBackground: " + decodedToken);

                driver.setUid(new JSONObject(decodedToken).getString("sub"));


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
