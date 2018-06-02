package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.util.ArrayList;

import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Domain.MRMFormulier;
import theekransje.douaneapp.Interfaces.OnStatusUpdate;

/**
 * Created by Sander on 5/24/2018.
 */

//Query status update. Deze worden afgevuurd door een achtergrond thread. Stop hier dus geen while lus in!

public class AsyncGetStatusUpdate extends AsyncTask<Freight,ArrayList<String>,Freight> {
    private static final String TAG = "AsyncGetStatusUpdate";
    private final String endPoint = "/tbd";
    private ApiHelper helper;
    private OnStatusUpdate listener;
    public AsyncGetStatusUpdate(OnStatusUpdate listener){
        this.helper = new ApiHelper(endPoint,APIMethodes.POST);
        this.listener = listener;
    }


    @Override
    protected Freight doInBackground(Freight... freight) {
        HttpURLConnection connection = helper.getConnection();
        try {
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            JSONObject mrns = new JSONObject();
            mrns.put("mrn",freight[0].getMrmFormulier().Mrn);
            Log.e(TAG,mrns.toString());
            osw.write(mrns.toString());
            Log.e(TAG,helper.convertIStoString(connection.getInputStream()));
            JSONArray returnedData = new JSONArray(connection);
            Freight newFreight = new Freight();
            MRMFormulier formulier = new MRMFormulier();
            JSONObject data = returnedData.getJSONObject(0);
            formulier.Mrn = data.getString("mrn");
            formulier.AantalArtikelen=data.getInt("aantalartikelen");
            formulier.Afzender = data.getString("afzender");
            formulier.Currency = data.getString("currency");
            formulier.DateTime = (long) data.get("time");
            formulier.Ontvanger = data.getString("ontvanger");
            formulier.Opdrachtgever = data.getString("opdrachtgever");
            formulier.Reference = data.getString("reference");
            formulier.TotaalBedrag = data.getDouble("totaalbedrag");
            formulier.TotaalGewicht = data.getDouble("totaalgewicht");
            newFreight.setMrmFormulier(formulier);
            listener.onStatusUpdateAvail(newFreight);
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
        }finally {
            connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Freight arrayList) {
        listener.onStatusUpdateAvail(arrayList);
        super.onPostExecute(arrayList);
    }
}
