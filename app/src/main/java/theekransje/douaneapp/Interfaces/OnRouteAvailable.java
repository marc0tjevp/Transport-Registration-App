package theekransje.douaneapp.Interfaces;

import java.util.ArrayList;

import theekransje.douaneapp.Domain.Coordinate;

/**
 * Created by Sander on 5/24/2018.
 */

public interface OnRouteAvailable {
    public void OnRouteAvailable(ArrayList<Coordinate> coordinates);
}
