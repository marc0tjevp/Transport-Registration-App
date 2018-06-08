package theekransje.douaneapp.Domain;

import android.os.Handler;
import android.util.Log;

import theekransje.douaneapp.Interfaces.OnTimeChange;

public class TimerClock implements Runnable{
    private Handler delayHandler;
    private static final String TAG = "TimerClock";
    private int hours;
    private int minutes;
    private int seconds;
    private OnTimeChange listener;
    private TimerClock(){
        seconds=0;
        minutes = 0;
        hours=0;
    }
    public TimerClock(Handler delayHandler){
        this();
        this.delayHandler = delayHandler;
    }

    public TimerClock(Handler delayHandler,OnTimeChange listener){
        this(delayHandler);
        this.listener = listener;
    }

    @Override
    public void run() {
        Log.d(TAG,"running Timer");
        if (hours==4&&minutes==15&&listener!=null){
            listener.toast();
        }
        if (seconds==59){
            seconds=0;
            minutes++;
        }else {
            seconds++;
        }
        if (minutes==59) {
            minutes = 0;
            hours = hours + 1;
        }
        Log.d(TAG, "Current time:"+getTime());
        if (listener!=null) {
            listener.onTimeChange(getTime());
        }
        delayHandler.postDelayed(this,1000);
    }

    public String getTime(){
        Log.d(TAG,"Current time: "+hours+":"+minutes);
        String output = "";
        if (hours<10){
            output="0"+hours;
        }else {
            output=output+hours;
        }
        output=output+":";
        if (minutes<10){
            output=output+"0"+minutes;
        } else {
            output=output+minutes;
        }
        output=output+":";
        if (seconds<10){
            output=output+"0"+seconds;
        } else {
            output=output+seconds;
        }
        return output;
    }
}