package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import theekransje.douaneapp.Domain.APITask;
import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Interfaces.OnTimesReady;
import theekransje.douaneapp.Persistence.DBHelper;

public class AsyncGetDrivenTimes extends AsyncTask{
    private static final String TAG = "AsyncSendTime";
    private final String endPoint= "drivetimes/getdrivebyid";
    private ApiHelper helper;
    private OnTimesReady listener;

    public AsyncGetDrivenTimes(OnTimesReady listener) {
        this.listener = listener;
        helper = new ApiHelper(endPoint,APIMethodes.GET);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            HttpURLConnection conn = helper.getConnection();
            int statusCode = conn.getResponseCode();
            Log.d(TAG, "doInBackground: statuscode: " + statusCode);

            if (statusCode == 200) {
                ArrayList<String> dates = new ArrayList<>();
                String outputFromthing=helper.convertIStoString(conn.getInputStream());
                JSONObject times = new JSONObject(outputFromthing);

                JSONArray array = (JSONArray) times.get("message");
                Log.d(TAG,array+"");
                for (int i = 0;i<array.length();i++){
                    JSONObject time = array.getJSONObject(i);
                    Log.d(TAG,time+"");
                    Date startTime = new Date(time.getLong("startTime"));
                    Date endTime = new Date(time.getLong("endTime"));
                    String type = time.getString("type");
                    dates.add(startTime+"\n"+type+"\n"+endTime);
                }
                listener.onTimesReady(dates);
            }else if (statusCode == 401) {
            Log.d(TAG, "doInBackground: Login failed");


        } else {
            Log.d(TAG, "doInBackground: " + "Undefined error");
            Thread.sleep(200);
        }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
