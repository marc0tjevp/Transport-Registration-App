package theekransje.douaneapp.Interfaces;

import java.util.ArrayList;

import theekransje.douaneapp.Domain.Freight;

/**
 * Created by Sander on 5/24/2018.
 */

public interface OnStatusUpdate {
    ArrayList<Freight> getFreights();

    void onStatusUpdateAvail(Freight freight);
}
