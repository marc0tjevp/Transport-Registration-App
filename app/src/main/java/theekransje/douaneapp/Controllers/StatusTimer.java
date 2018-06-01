package theekransje.douaneapp.Controllers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import theekransje.douaneapp.API.AsyncGetStatusUpdate;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.Interfaces.OnStatusUpdate;

public class StatusTimer extends TimerTask {
    private OnStatusUpdate listener;
    public StatusTimer(OnStatusUpdate listener){
        this.listener = listener;
        Timer timer = new Timer();
        timer.schedule(this,0,30000);
    }
    @Override
    public void run() {
        for(Freight freight:listener.getFreights()) {
            new AsyncGetStatusUpdate(listener).execute(freight);
        }
    }
}
