package theekransje.douaneapp.Controllers;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import theekransje.douaneapp.R;

public class FreightAdapter extends BaseAdapter {

    private List<String> mrns;
    private LayoutInflater mInflator;

    public FreightAdapter(List<String> mrnList, LayoutInflater layoutInflater){
        mrns = mrnList;
        mInflator = layoutInflater;
    }

    public void addMRN(String s){
        mrns.add(s);
    }
    public void removeMRN(int position){
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
            viewHolder.layout = convertView.findViewById(R.id.layout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String mrn = mrns.get(position);
        viewHolder.mrn = convertView.findViewById(R.id.mrn);
        viewHolder.mrn.setText(mrn);
        return convertView;
    }

    private static class ViewHolder implements View.OnClickListener {
        public TextView mrn;
        public RelativeLayout layout;

        @Override
        public void onClick(View v) {

        }
    }


}
