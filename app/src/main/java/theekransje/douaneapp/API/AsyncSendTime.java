package theekransje.douaneapp.API;

import android.os.AsyncTask;

public class AsyncSendTime extends AsyncTask {
    private static final String TAG = "AsyncSendTime";
    private final String endPoint = "/tbd";
    private ApiHelper helper;

    public AsyncSendTime(){
        helper = new ApiHelper(endPoint,APIMethodes.POST);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        return null;
    }
}
