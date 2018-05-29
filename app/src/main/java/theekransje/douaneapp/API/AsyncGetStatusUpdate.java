package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Interfaces.OnStatusUpdate;

/**
 * Created by Sander on 5/24/2018.
 */

//Query status update. Deze worden afgevuurd door een achtergrond thread. Stop hier dus geen while lus in!

public class AsyncGetStatusUpdate extends AsyncTask<ArrayList<String>,ArrayList<String>,ArrayList<Freight>> {
    private static final String TAG = "AsyncGetStatusUpdate";
    private final String endPoint = "/tbd";
    private ApiHelper helper;
    private OnStatusUpdate listener;
    public AsyncGetStatusUpdate(OnStatusUpdate listener){
        this.helper = new ApiHelper(endPoint,APIMethodes.POST);
        this.listener = listener;
    }

    @Override
    protected ArrayList<Freight> doInBackground(ArrayList<String>... arrayLists) {
        HttpURLConnection connection = helper.getConnection();
        try {
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            JSONArray mrns=new JSONArray();
            for (String nextLine:arrayLists[0]) {
                mrns.put(nextLine);
            }
            JSONObject object = new JSONObject();
            object.put("mrns",mrns);
            Log.e(TAG,object.toString());
            osw.write(object.toString());
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
        }finally {
            connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Freight> arrayList) {
        listener.onStatusUpdateAvail(arrayList);
        super.onPostExecute(arrayList);
    }
}
