package theekransje.douaneapp.API;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Date;

import theekransje.douaneapp.Controllers.DrivingState;

public class AsyncSendTime extends AsyncTask {
    private static final String TAG = "AsyncSendTime";
    private final String endPoint = "drivetimes/senddrive";
    private ApiHelper helper;

    public AsyncSendTime(){
        helper = new ApiHelper(endPoint,APIMethodes.POST);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            HttpURLConnection conn = helper.getConnection();
            JSONObject object = new JSONObject();
            object.put("startTime", objects[0]);
            object.put("endTime", objects[1]);
            object.put("type",objects[2]);
            object.put("mrn",objects[3]);
            conn.getOutputStream().write(object.toString().getBytes());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
