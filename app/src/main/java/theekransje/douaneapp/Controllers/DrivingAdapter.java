package theekransje.douaneapp.Controllers;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import theekransje.douaneapp.R;

public class DrivingAdapter extends BaseAdapter {
    private ArrayList<String> dates;
    private LayoutInflater inflater;

    public DrivingAdapter(ArrayList<String> dateList, LayoutInflater layoutInflater){
        dates = dateList;
        inflater = layoutInflater;
    }
    public void addDate(String date){
        dates.add(date);
    }
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.driving_row,null);

            holder = new ViewHolder();
            holder.view = convertView.findViewById(R.id.time_text);
            holder.layout = convertView.findViewById(R.id.freight_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String date = dates.get(position);
        holder.view.setText(date);
        return convertView;
    }
    private static class ViewHolder implements View.OnClickListener {
        public TextView view;
        public ConstraintLayout layout;

        @Override
        public void onClick(View v) {
            Log.d("TEST", "onClick: " + v.getId());
        }


    }
}
