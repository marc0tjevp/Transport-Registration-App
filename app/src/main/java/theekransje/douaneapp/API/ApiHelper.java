package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import theekransje.douaneapp.Domain.Driver;

/**
 * Created by Sander on 5/24/2018.
 */

public class ApiHelper {
    private static final String TAG = "ApiHelper";
    public static final String API_URL = "http://192.168.178.10:8080/";
    public static String token;



    private String endpoint;

    private APIMethodes apiMethode;

    private HttpURLConnection conn;

    public ApiHelper(String endpoint, APIMethodes apiMethode) {
        this.endpoint = endpoint;
        this.apiMethode = apiMethode;
    }


    public HttpURLConnection getConnection() {
        try {

            URL url = new URL(API_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(apiMethode.toString());
            conn.setRequestProperty("Content-Type", "application/json");

            if (token != null){
                conn.setRequestProperty("Authorization", "Bearer " + token );
            }

            if (apiMethode == APIMethodes.POST){
                conn.setDoOutput(true);
            }

            conn.setUseCaches(false);
            conn.setChunkedStreamingMode(100);
            conn.connect();

            Log.d(TAG, "getConnection: " + conn.toString());
            Log.d(TAG, "getConnection: " + conn.getRequestMethod());
            Log.d(TAG, "getConnection: " + conn.getRequestProperty("Authorization"));
            Log.d(TAG, "getConnection: " + conn.getRequestProperty("Content-Type"));

            return conn;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }


    public static String convertIStoString(InputStream inputStream) {
        Log.d("", "convertIStoString: called");

        try {
            ///////////////////CONVERT InputStream to String////////////////////////

            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String nextline;
            StringBuilder sb = new StringBuilder();

            while ((nextline = bufferedReader.readLine()) != null) {
                sb.append(nextline);
            }

            bufferedReader.close();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;


    }


}
