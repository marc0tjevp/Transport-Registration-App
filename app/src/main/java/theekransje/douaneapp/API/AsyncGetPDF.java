
package theekransje.douaneapp.API;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.net.URL;

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


    private static final String TAG = "AsyncGetPDF";
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
        int count;
        if (Build.VERSION.SDK_INT >= 23) {
            if (app.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");

            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(app, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }



        try {

            ApiHelper apiHelper = new ApiHelper(endPoint + mrn, APIMethodes.GET);

            String root = Environment.getExternalStorageDirectory().toString();

            System.out.println("Downloading");
            URL url = new URL(apiHelper.getUrl());

            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file

            File dir = new File(root + "/douane/");
            dir.mkdir();

            File target = new File(root + "/douane/" + mrn + ".pdf");
            Log.d(TAG, "doInBackground: " + target.getAbsolutePath());
            if (target.exists()) {
                Log.d(TAG, "doInBackground: file exists. No download");
                url = null;
                conection = null;
                listener.OnPDFAvail(target.getAbsolutePath(), mrn);
                return null;
            }
            Log.d(TAG, "doInBackground: starting download");
            OutputStream output = new FileOutputStream(target);
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            url = null;
            conection = null;
            listener.OnPDFAvail(target.getAbsolutePath(), mrn);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        } finally {

        }

        return null;
    }
}
