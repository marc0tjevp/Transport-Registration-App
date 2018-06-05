package theekransje.douaneapp.Domain;

import java.io.Serializable;

/**
 * Created by Sander on 5/24/2018.
 */

public class Freight implements Serializable {
    private static final String TAG = "Freight";
    private MRMFormulier mrmFormulier;
    private int ID;
    private DouaneStatus douaneStatus;


    public static String getTAG() {
        return TAG;
    }


    public MRMFormulier getMrmFormulier() {
        return mrmFormulier;
    }

    public void setMrmFormulier(MRMFormulier mrmFormulier) {
        this.mrmFormulier = mrmFormulier;
    }


    public int getID() {
        return ID;
    }


    public DouaneStatus getDouaneStatus() {
        return douaneStatus;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public void setDouaneStatus(DouaneStatus douaneStatus) {
        this.douaneStatus = douaneStatus;

    }
}
