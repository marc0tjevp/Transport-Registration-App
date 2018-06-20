package theekransje.douaneapp.Domain;

import android.os.Handler;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import theekransje.douaneapp.Interfaces.OnTimeChange;

public class TimerClock implements Runnable{
    private Handler delayHandler;
    private static final String TAG = "TimerClock";
    private int hours;
    private int minutes;
    private int seconds;
    private OnTimeChange listener;
    private Date startDate;
    private TimerClock(){
        seconds=0;
        minutes = 0;
        hours=0;
        startDate = Calendar.getInstance().getTime();
    }
    public TimerClock(Handler delayHandler){
        this();
        this.delayHandler = delayHandler;
    }
    public TimerClock(TimerClock clock){
        this.hours = clock.hours;
        this.minutes = clock.minutes;
        this.seconds = clock.seconds;
        this.delayHandler = clock.delayHandler;
        this.listener = clock.listener;
        startDate = Calendar.getInstance().getTime();
    }

    public TimerClock(Handler delayHandler,OnTimeChange listener){
        this(delayHandler);
        this.listener = listener;
    }

    @Override
    public void run() {


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

        if (listener!=null) {
            listener.onTimeChange(getTime());
        }
        delayHandler.postDelayed(this,1000);
    }

    public String getTime(){
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
    public Date[] getDate(){
        Date[] output = {startDate,Calendar.getInstance().getTime()};
        return output;
    }
}