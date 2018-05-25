package theekransje.douaneapp.API;

import android.os.AsyncTask;

/**
 * Created by Sander on 5/24/2018.
 */

//Query status update. Deze worden afgevuurd door een achtergrond thread. Stop hier dus geen while lus in!

public class AsyncGetStatusUpdate extends AsyncTask {
    private static final String TAG = "AsyncGetStatusUpdate";
    private final String endPoint = "/tbd";

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
