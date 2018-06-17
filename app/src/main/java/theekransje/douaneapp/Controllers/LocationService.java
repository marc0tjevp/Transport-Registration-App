package theekransje.douaneapp.Controllers;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import theekransje.douaneapp.API.AsyncSendLocationPing;
import theekransje.douaneapp.R;


public class LocationService extends Service {
    private String mrn = null;

    private NotificationManager notificationManager;
    private int NOTIFICATION = 666;

    private final static String TAG = "LocationService";

    public class LocationTrackingBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private PowerManager.WakeLock wakeLock;
    private boolean isTracking;

    @Override
    public void onCreate() {
        Log.d(TAG, "creating service");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.isTracking = false;
        Log.d(TAG, "Setting up location request");
        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(5000);
        this.locationRequest.setFastestInterval(3000);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                if (result == null) {
                    Log.d(TAG, "no location");
                    Toast.makeText(LocationService.this, "No access to location", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    for (Location location : result.getLocations()) {
                        Log.d(TAG, "sending location");
                        sendLocation(location);
                    }
                }
            }
        };

        // Display a notification about us starting.
        showNotification();

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "douanenApp");
            wakeLock.acquire();
        }
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        notificationManager.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, "Backend service stopped", Toast.LENGTH_SHORT).show();
        wakeLock.release();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mrn = intent.getStringExtra("mrn");
        Log.d(TAG, mrn);
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocationTrackingBinder();

    private void showNotification() {
        Log.d(TAG, "making notification");

        // Set the info for the views that show in the notification panel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(getApplicationContext(), "DOEANENAPPTRANSIT")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service running")
                    .setAutoCancel(true);

            Notification notification = builder.build();
            startForeground(NOTIFICATION, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "DOEANENAPPTRANSIT")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Background service started")
                    .setContentText("Background service started");

            Notification notification = builder.build();
            startForeground(NOTIFICATION, notification);
        }
    }


    public void startLocationTracking() {
        if (!isTracking) {
            Log.d(TAG, "Started tracking location");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Geen toegang tot locatie", Toast.LENGTH_LONG).show();
                return;
            }
            this.fusedLocationProviderClient.requestLocationUpdates(this.locationRequest, this.locationCallback, Looper.myLooper());
            isTracking = true;
        }
    }

    public void stopLocationTracking() {
        if(isTracking) {
            this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallback);
            isTracking = false;
        }
    }

    public void sendLocation(final Location location) {
        AsyncTask<Void, Void, Void> task = new AsyncSendLocationPing(mrn, location.getLongitude(), location.getLatitude());
        task.execute();
    }


}

