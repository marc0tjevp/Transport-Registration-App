package theekransje.douaneapp.API;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Date;

import theekransje.douaneapp.Controllers.DrivingState;

public class AsyncSendTime extends AsyncTask {
    private static final String TAG = "AsyncSendTime";
    private final String endPoint = "/senddrive";
    private ApiHelper helper;

    public AsyncSendTime(){
        helper = new ApiHelper(endPoint,APIMethodes.POST);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            HttpURLConnection conn = helper.getConnection();
            JSONObject object = new JSONObject();
            object.put("startTime", ((Date[]) objects[0])[0].toString());
            object.put("endTime", ((Date[]) objects[0])[1].toString());
            switch ((DrivingState) objects[1]) {
                case Paused:object.put("type","Pauze");
                    break;
                case Driving:object.put("type","Rijden");
                    break;
                case Stopped:object.put("type","Rijden");
                    break;
            }
            object.put("mrn",objects[2]);
            conn.getOutputStream().write(object.toString().getBytes());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
