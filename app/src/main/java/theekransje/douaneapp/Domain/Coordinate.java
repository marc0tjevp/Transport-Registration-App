package theekransje.douaneapp.Domain;

import java.io.Serializable;

/**
 * Created by Sander on 5/24/2018.
 */

public class Coordinate implements Serializable {
    private static final String TAG = "Coordinate";
    private long nanoTime;
    private long x;
    private long y;

    public Coordinate(long nanoTime, long x, long y) {
        this.nanoTime = nanoTime;
        this.x = x;
        this.y = y;
    }

    public Coordinate(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getNanoTime() {
        return nanoTime;
    }

    public void setNanoTime(long nanoTime) {
        this.nanoTime = nanoTime;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }
}
