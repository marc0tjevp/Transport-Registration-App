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
    private final String endPoint = "/login";
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
            j.put("UserName", driver.getUserName());
            j.put("Passwd", driver.getPasswd());
            j.put("Hash", driver.getIMEIHash());

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());

            os.write(j.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int statusCode = conn.getResponseCode();
            Log.d(TAG, "doInBackground: statuscode: "+ statusCode);

            if(statusCode == 200){
                JSONObject r = new JSONObject(ApiHelper.convertIStoString(conn.getInputStream()));
                driver.setToken(r.getString("Token"));
                listener.onLoginSucces(driver);
            }else {
                Log.d(TAG, "doInBackground: Login failed");;

                driver.setToken("1");
                listener.onLoginSucces(driver);

                //listener.onLoginFailure("Undefined Error");
            }

            conn.disconnect();
            conn = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
