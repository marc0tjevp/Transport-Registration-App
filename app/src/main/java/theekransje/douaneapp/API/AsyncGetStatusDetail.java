package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Domain.MRNFormulier;
import theekransje.douaneapp.Interfaces.OnStatusDetailAvail;

/**
 * Created by Sander on 5/24/2018.
 */
///////Gedetaileerde status ophalen

public class AsyncGetStatusDetail extends AsyncTask {
    private static final String TAG = "AsyncGetStatusDetail";
    private final String endPoint = "customs/form/";

    private String mrn;
    private OnStatusDetailAvail listener;


    public AsyncGetStatusDetail(String mrn, OnStatusDetailAvail listener) {
        this.mrn = mrn;
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            Log.d(TAG, "doInBackground: called");

            ApiHelper apiHelper = new ApiHelper(endPoint + mrn , APIMethodes.GET);

            HttpURLConnection conn = apiHelper.getConnection();


            int statusCode = conn.getResponseCode();
            Log.d(TAG, "doInBackground: statuscode: " + statusCode);

            if (statusCode == 200) {
                Log.d(TAG, "doInBackground: ");


                JSONObject reply = new JSONObject(ApiHelper.convertIStoString(conn.getInputStream()));
                JSONObject jsonObject = reply.getJSONObject("message");



                MRNFormulier mrnFormulier = new MRNFormulier();
                mrnFormulier.setMrn(mrn);
                mrnFormulier.setArticleAmount(jsonObject.getInt("articleAmount"));
                mrnFormulier.setSender(jsonObject.getString("sender"));
                mrnFormulier.setReceiver(jsonObject.getString("receiver"));
                mrnFormulier.setCurrency(jsonObject.getString("currency"));
                mrnFormulier.setTotalWeight(jsonObject.getDouble("totalWeight"));
                mrnFormulier.setTotalPRice(jsonObject.getDouble("totalAmount"));
                mrnFormulier.setClient(jsonObject.getString("client"));
                mrnFormulier.setReference(jsonObject.getString("reference"));
                mrnFormulier.DateTime = jsonObject.getString("dateTime");
                mrnFormulier.setVerzenderAdres(jsonObject.getString("addressOrigin"));
                mrnFormulier.setOntvangstAdres(jsonObject.getString("addressDestination"));

                Freight freight = new Freight();
                freight.setMRNFormulier(mrnFormulier);
                freight.setDouaneStatus(StatusDecoder.decodeStatusCode(jsonObject.getInt("declarationStatus")));


                Log.d(TAG, "doInBackground: MRN form received " + mrn);

                listener.OnStatusDetailAvail(freight);

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

