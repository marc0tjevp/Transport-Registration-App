package theekransje.douaneapp.Interfaces;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

public interface OnTimesReady {
    void onTimesReady(ArrayList<String> dates);
    Context getContext();
}
