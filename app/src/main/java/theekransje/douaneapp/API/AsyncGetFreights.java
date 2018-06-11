package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.util.Base64Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Interfaces.OnFreightListAvail;

/**
 * Created by Sander on 5/24/2018.
 */

///Alle toegewezen vrachten ophalen


public class AsyncGetFreights extends AsyncTask {
    private static final String TAG = "AsyncGetFreights";
    private final String endPoint = "company/driver/get";

    private OnFreightListAvail listener;
    private Driver driver;



    public AsyncGetFreights(OnFreightListAvail listener, Driver driver) {
        this.listener = listener;
        this.driver = driver;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            Log.d(TAG, "doInBackground: called");
            
            ApiHelper apiHelper = new ApiHelper(endPoint, APIMethodes.GET);

            HttpURLConnection conn = apiHelper.getConnection();





            int statusCode = conn.getResponseCode();
            Log.d(TAG, "doInBackground: statuscode: " + statusCode);

            if (statusCode == 200) {
                Log.d(TAG, "doInBackground: ");

                JSONObject jsonObject = new JSONObject( ApiHelper.convertIStoString(conn.getInputStream()));
                JSONArray jsonArray = jsonObject.getJSONArray("message");


                ArrayList<String> mrns = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject j = jsonArray.getJSONObject(i);
                    mrns.add(j.getString("mrn"));
                }

                Log.d(TAG, "doInBackground: MRNS found " + mrns.toString());

                listener.OnFreightListAvail(mrns);

            } else if (statusCode == 401) {
                Log.d(TAG, "doInBackground: Login failed");


            } else {
                Log.d(TAG, "doInBackground: " + "Undefined error");
            }

            conn.disconnect();
            conn = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
