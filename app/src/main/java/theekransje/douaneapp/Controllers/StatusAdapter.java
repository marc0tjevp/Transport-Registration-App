package theekransje.douaneapp.Controllers;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import theekransje.douaneapp.Domain.DouaneStatus;
import theekransje.douaneapp.Domain.Freight;
import theekransje.douaneapp.R;

/**
 * Created by Sander on 5/28/2018.
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView status;
        public TextView sender;
        public TextView recipient;
        public TextView mrn;
        public ConstraintLayout cl;
        public ImageView thumb;

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;

            this.status = itemView.findViewById(R.id.status_row_status);
            this.sender = itemView.findViewById(R.id.status_row_sender);
            this.recipient = itemView.findViewById(R.id.status_row_recipient);
            this.mrn = itemView.findViewById(R.id.status_row_mrn);
            this.cl = itemView.findViewById(R.id.status_row_cl);
            this.thumb = itemView.findViewById(R.id.status_row_thumb);

        }
    }


    private ArrayList<Freight> mData;
    private Context mContext;

    public StatusAdapter(ArrayList<Freight> freights, Context context) {
        this.mData = freights;
        this.mContext = context;
    }

    @NonNull
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.status_row, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Freight freight = mData.get(position);

        if (position % 2 == 0) {
            holder.cl.setBackgroundColor(Color.LTGRAY);
        }


        holder.mrn.setText("" + freight.getMrmFormulier().getMrn());
        holder.recipient.setText("" + freight.getMrmFormulier().getOntvanger());
        holder.sender.setText("" + freight.getMrmFormulier().getAfzender());
        holder.status.setText("" + freight.getDouaneStatus().toString());


        if (mData.get(position).getDouaneStatus().equals(DouaneStatus.LOSSEN_OK) || mData.get(position).getDouaneStatus().equals(DouaneStatus.VERTREK_OK)) {
            holder.thumb.setImageDrawable(mContext.getDrawable(R.drawable.ic_thumb_up_black_24dp));
            holder.thumb.setVisibility(View.VISIBLE);
        } else if (mData.get(position).getDouaneStatus().equals(DouaneStatus.VERZONDEN)) {
            holder.thumb.setImageDrawable(mContext.getDrawable(R.drawable.ic_timer_black_24dp));
            holder.thumb.setVisibility(View.VISIBLE);
        } else {
            holder.thumb.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}

