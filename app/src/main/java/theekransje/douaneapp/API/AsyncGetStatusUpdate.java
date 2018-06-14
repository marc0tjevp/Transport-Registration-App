package theekransje.douaneapp.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Domain.MRNFormulier;
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
            mrns.put("mrn",freight[0].getMRNFormulier().getMrn());
            Log.e(TAG,mrns.toString());
            osw.write(mrns.toString());
            Log.d(TAG,helper.convertIStoString(connection.getInputStream()));
            JSONObject data = new JSONObject(helper.convertIStoString(connection.getInputStream()));
            Freight newFreight = new Freight();
            MRNFormulier formulier = new MRNFormulier();

            formulier.setMrn(data.getString("mrn"));
            formulier.setArticleAmount(data.getInt("aantalartikelen"));
            formulier.setSender(data.getString("afzender"));
            formulier.setCurrency(data.getString("currency"));
            formulier.setDateTime((String) data.get("time"));
            formulier.setReceiver( data.getString("ontvanger"));
            formulier.setClient(data.getString("opdrachtgever"));
            formulier.setReference(data.getString("reference"));
            formulier.setTotalPRice(data.getDouble("totaalbedrag"));
            formulier.setTotalWeight(data.getDouble("totaalgewicht"));
            formulier.setOrigin(data.getString("addressOrigin"));
            formulier.setDestination(data.getString("addressDestination"));
            newFreight.setMRNFormulier(formulier);
            listener.onStatusUpdateAvail(newFreight);
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
        }finally {
            connection.disconnect();
        }
        return null;
    }
}
