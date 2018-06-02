package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;

/**
 * Created by Sander on 5/28/2018.
 */

public class Navbar {
    private static final String TAG =  "NAVBAR LOGIC";

    public static void goToStatus(Context context, Driver driver, ArrayList<Freight> freights){
        Log.d(TAG, "goToStatus: Fired");
        
        Intent intent = new Intent(context, StatusActivity.class);

        intent.putExtra("DRIVER", driver);
        intent.putExtra("FREIGHTS", freights);

        context.startActivity(intent);
    }

    public static void goToFreights(Context context, Driver driver, ArrayList<Freight> freights){
        Log.d(TAG, "goToFreights: FIRED");
        Intent intent = new Intent(context, FreightActivity.class);

        intent.putExtra("DRIVER", driver);
        intent.putExtra("FREIGHTS", freights);

        context.startActivity(intent);
    }

    public static void goToDrive(Context context, Driver driver, ArrayList<Freight> freights){
        Log.d(TAG, "goToDrive: FIRED");
        Intent intent = new Intent(context, DrivingActivity.class);

        intent.putExtra("DRIVER", driver);
        intent.putExtra("FREIGHTS", freights);

        context.startActivity(intent);
    }
}
