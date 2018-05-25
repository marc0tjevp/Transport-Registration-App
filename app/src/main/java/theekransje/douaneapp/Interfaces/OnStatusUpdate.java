package theekransje.douaneapp.Interfaces;

import theekransje.douaneapp.Domain.Freight;

/**
 * Created by Sander on 5/24/2018.
 */

public interface OnStatusUpdate {
    public void onStatusUpdateAvail(Freight vracht);
}
