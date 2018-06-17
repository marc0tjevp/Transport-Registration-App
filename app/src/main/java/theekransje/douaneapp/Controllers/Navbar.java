package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import theekransje.douaneapp.Domain.Driver;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.R;

/**
 * Created by Sander on 5/28/2018.
 */

public class Navbar {
    private static final String TAG = "NAVBAR LOGIC";

    public static void goToStatus(Context context, Driver driver, ArrayList<Freight> freights) {
        Log.d(TAG, "goToStatus: Fired");

        Intent intent = new Intent(context, StatusActivity.class);

        intent.putExtra("DRIVER", driver);
        intent.putExtra("FREIGHTS", freights);

        context.startActivity(intent);
    }


    public static boolean goToDrive(Context context, Driver driver, ArrayList<Freight> freights) {
        if (freights != null && freights.size() > 0) {
            Log.d(TAG, "goToDrive: FIRED");
            Intent intent = new Intent(context, DrivingActivity.class);

            intent.putExtra("DRIVER", driver);
            intent.putExtra("FREIGHTS", freights);

            context.startActivity(intent);
            return true;
        } else {
            Toast.makeText(context, R.string.no_freights_selected, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
