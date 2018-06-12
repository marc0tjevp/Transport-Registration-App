
package theekransje.douaneapp.API;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import theekransje.douaneapp.Domain.DouaneStatus;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Domain.MRNFormulier;
import theekransje.douaneapp.Interfaces.OnPDFAvail;
import theekransje.douaneapp.Interfaces.OnStatusDetailAvail;

/**
 * Created by Sander on 5/24/2018.
 */
///////Gedetaileerde status ophalen


public class AsyncGetPDF extends AsyncTask {


    private static final String TAG = "AsyncGetStatusDetail";
    private final String endPoint = "customs/status/";

    private String mrn;
    private OnPDFAvail listener;
    private AppCompatActivity app;


    public AsyncGetPDF(String mrn, OnPDFAvail listener, AppCompatActivity appCompatActivity) {
        this.mrn = mrn;
        this.listener = listener;
        this.app = appCompatActivity;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {

            Log.d(TAG, "doInBackground: called");
            try {

                String s = ApiHelper.convertIStoString(app.openFileInput(mrn + ".pdf"));
                listener.OnPDFAvail(s,mrn);

            } catch (Exception e) {
                e.printStackTrace();

                try {


                    ApiHelper apiHelper = new ApiHelper(endPoint + mrn, APIMethodes.GET);

                    HttpURLConnection conn = apiHelper.getConnection();


                    int statusCode = conn.getResponseCode();
                    Log.d(TAG, "doInBackground: statuscode: " + statusCode);

                    if (statusCode == 200) {
                        Log.d(TAG, "doInBackground: ");






                        try {
                            FileOutputStream fileOutputStream = app.openFileOutput(mrn + ".pdf", Context.MODE_PRIVATE);
                            fileOutputStream.write(ApiHelper.convertIStoString(conn.getInputStream()).getBytes());
                            fileOutputStream.close();

                        } catch (Exception z) {
                            z.printStackTrace();
                        }


                        Log.d(TAG, "doInBackground: MRN form received " + mrn);

                        listener.OnPDFAvail(ApiHelper.convertIStoString(conn.getInputStream()), mrn);

                    } else if (statusCode == 401) {
                        Log.d(TAG, "doInBackground: Login failed");


                    } else {
                        Log.d(TAG, "doInBackground: " + "Undefined error");
                    }

                    conn.disconnect();
                    conn = null;

                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

