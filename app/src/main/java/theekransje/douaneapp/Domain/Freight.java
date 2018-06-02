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

<<<<<<< HEAD
=======
    public static String getTAG() {
        return TAG;
    }

>>>>>>> master
    public MRMFormulier getMrmFormulier() {
        return mrmFormulier;
    }

<<<<<<< HEAD
=======
    public void setMrmFormulier(MRMFormulier mrmFormulier) {
        this.mrmFormulier = mrmFormulier;
    }

>>>>>>> master
    public int getID() {
        return ID;
    }

<<<<<<< HEAD
    public DouaneStatus getDouaneStatus() {
        return douaneStatus;
    }
=======
    public void setID(int ID) {
        this.ID = ID;
    }

    public DouaneStatus getDouaneStatus() {
        return douaneStatus;
    }

    public void setDouaneStatus(DouaneStatus douaneStatus) {
        this.douaneStatus = douaneStatus;
    }
>>>>>>> master
}
