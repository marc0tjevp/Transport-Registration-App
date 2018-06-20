package theekransje.douaneapp.Controllers;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.R;

public class FreightAdapter extends BaseAdapter {

    private ArrayList<String> mrns;
    private LayoutInflater mInflator;
    private ArrayList<String> selected;
    private ArrayList<String> originalList;

    public FreightAdapter(ArrayList<String> mrnList, LayoutInflater layoutInflater, ArrayList<String> selected) {
        mrns = mrnList;
        mInflator = layoutInflater;
        this.selected = selected;
        this.originalList = mrnList;
    }
    public void Search(String text){
        if (text.length() > 4){
            mrns = new ArrayList<String>();
            for (String s:originalList
                 ) {
                if (s.contains(text)){
                    mrns.add(s);
                }
            }
        }else {
            mrns = originalList;
        }

    }

    public void addMRN(String s) {
        mrns.add(s);
    }

    public void removeMRN(int position) {
        mrns.remove(position);
    }

    @Override
    public int getCount() {
        return mrns.size();
    }

    @Override
    public Object getItem(int position) {
        return mrns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Creates new or uses an exiting
        if (convertView == null) {
            convertView = mInflator.inflate(R.layout.mrn_row, null);

            viewHolder = new ViewHolder();
            viewHolder.mrn = convertView.findViewById(R.id.mrn);
            viewHolder.layout = convertView.findViewById(R.id.freight_layout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String mrn = mrns.get(position);
        viewHolder.mrn = convertView.findViewById(R.id.mrn);
        viewHolder.mrn.setText(mrn);
//        viewHolder.layout.setBackgroundColor(Color.GREEN);
        return convertView;
    }

    private static class ViewHolder implements View.OnClickListener {
        public TextView mrn;
        public ConstraintLayout layout;

        @Override
        public void onClick(View v) {
            Log.d("TEST", "onClick: " + v.getId());
        }


    }


}
