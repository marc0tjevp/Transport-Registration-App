package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sander on 5/24/2018.
 */

public class AsyncSendLocationPing extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "AsyncSendLocationPing";
    private final String endPoint = "location/post";
    private String mrn;
    private double longitude;
    private double latitude;

    public AsyncSendLocationPing(String mrn, double longitude, double latitude){
        this.mrn = mrn;
        this.longitude = longitude;

        this.latitude = latitude;
    }


    @Override
    protected Void doInBackground(Void[] objects) {
        Log.d(TAG, "Doinbackground sending location");
        ApiHelper apiHelper = new ApiHelper(endPoint + "/" + mrn, APIMethodes.POST);
        Log.d(TAG, endPoint + "/" + mrn);
        HttpURLConnection conn = null;
        DataOutputStream os = null;
        try {
            conn = apiHelper.getConnection();
            os = new DataOutputStream(conn.getOutputStream());

            JSONObject j = new JSONObject();
            j.put("long", longitude);
            j.put("lat", latitude);
            Log.d(TAG, "Ping " + latitude + ", " + longitude);
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
            String currentTime = simpleDateFormat.format(date);
            j.put("dateTime", currentTime);

            os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(j.toString());
            os.flush();
            Log.d(TAG, "Body: " + j.toString());
            Log.d(TAG, "Status: " + conn.getResponseCode() + ", " + conn.getResponseMessage());
            if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                conn.getInputStream().close();
            } else {
                conn.getErrorStream().close();
            }
            os.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {}
            }
        }
        return null;
    }

}
