package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Interfaces.OnTimesReady;

public class AsyncGetDrivenTimes extends AsyncTask{
    private static final String TAG = "AsyncSendTime";
    private final String endPoint = "/tbd";
    private ApiHelper helper;
    private Driver driver;
    private OnTimesReady listener;

    public AsyncGetDrivenTimes(Driver driver, OnTimesReady listener) {
        helper = new ApiHelper(endPoint,APIMethodes.GET);
        this.driver = driver;
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        JSONObject j = new JSONObject();
        try {
            helper.token = driver.getToken();
            HttpURLConnection conn = helper.getConnection();

            int statusCode = conn.getResponseCode();
            Log.d(TAG, "doInBackground: statuscode: " + statusCode);

            if (statusCode == 200) {
                ArrayList<Date> dates = new ArrayList<>();
                JSONObject times = new JSONObject(helper.convertIStoString(conn.getInputStream()));
                JSONArray array = (JSONArray) times.get("message");
                for (int i = 0;array.length()<i;i++){
                    JSONObject time = array.getJSONObject(i);
                    SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
                    Date date = parser.parse(time.getString("dateTime"));
                    dates.add(date);
                }
                listener.onTimesReady(dates);
            }else if (statusCode == 401) {
            Log.d(TAG, "doInBackground: Login failed");


        } else {
            Log.d(TAG, "doInBackground: " + "Undefined error");
        }
        } catch (Exception e){

        }
        return null;
    }
}
