package theekransje.douaneapp.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import theekransje.douaneapp.R;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<String> l = null;
    private ArrayList<String> arraylist;

    public ListViewAdapter(Context context, List<String> l) {
        mContext = context;
        this.l = l;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(l);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return l.size();
    }

    @Override
    public String getItem(int position) {
        return l.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);

            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(l.get(position).toString());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        l.clear();
        if (charText.length() == 0) {
            l.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if ((wp).toLowerCase(Locale.getDefault()).contains(charText)) {
                    l.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}