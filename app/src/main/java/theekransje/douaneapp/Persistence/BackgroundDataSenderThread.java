package theekransje.douaneapp.Persistence;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;

import theekransje.douaneapp.API.ApiHelper;
import theekransje.douaneapp.Domain.APITask;

/**
 * Created by Sander on 5/24/2018.
 */

//////Achtergrond Thread welke de data verzend zodra er internet verbinding is. Na succesvolle verzending verwijdering uit db

public class BackgroundDataSenderThread extends Thread {
    private static final String TAG = "BackgroundDataSenderThr";
    private DBHelper dbHelper;
    private Context context;
    public static BackgroundDataSenderThread thread;
    private boolean kill = false;

    public BackgroundDataSenderThread(Context context) {
        dbHelper = new DBHelper(context);

    }


    @Override
    public void run() {
        super.run();

        while (!kill) {
            try {

                while (!kill) {
                    if (dbHelper.getAllTasks().size() == 0) {

                        Log.d(TAG, "run: NO DATA TO SEND");
                        break;
                    }



                    Log.d(TAG, "BackgroundDataSenderThread: Trying to transmit data");

                    for (APITask a : dbHelper.getAllTasks()
                            ) {
                        Log.d(TAG, "Found task in DB: ID:" + a.getId() + " Method:" + a.getApiMethod() + " Endpoint:" + a.getEndpoint() + " Object: " + a.getJSONOBJECT().toString());

                        ApiHelper ah = new ApiHelper(ApiHelper.API_URL + a.getEndpoint(), a.getApiMethod());
                        try {
                            HttpURLConnection conn = ah.getConnection();

                            DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                            ////////////insert token info
                            os.write(a.getJSONOBJECT().toString().getBytes("UTF-8"));
                            os.flush();
                            os.close();

                            int statusCode = conn.getResponseCode();
                            Log.d(TAG, "backgroundsender: statuscode: " + statusCode);

                            if (statusCode == 200) {
                                Log.d(TAG, "Succesful. Deleting task: ID:" + a.getId() + " Method:" + a.getApiMethod() + " Endpoint:" + a.getEndpoint() + " Object: " + a.getJSONOBJECT().toString());
                                dbHelper.removeTask(a);
                            } else {
                                Log.d(TAG, "backgroundsender: failed");
                                ;
                            }

                            conn.disconnect();
                            conn = null;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Thread.sleep(1000);
                    }


                }
                Log.d(TAG, "run: holding for 20s");
                Thread.sleep(20000);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Log.d(TAG, "run: THREAD KILLED");

    }

    public void endThread(){
        this.kill = true;
    }
}
