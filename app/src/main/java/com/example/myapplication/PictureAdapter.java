package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guojunustb.library.WeekViewEvent;

import java.util.List;

class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder> {
    private List<ScheduleItem> mEvent;
    private Context context;


    public PictureAdapter(Context context, List<ScheduleItem> list) {
        this.context = context;
        this.mEvent = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.picture_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ScheduleItem event = mEvent.get(position);
        holder.pDate.setText(event.getIstarttime().substring(0,10));
        holder.pTime.setText(event.getIstarttime().substring(11,16) + "-" + event.getIendtime().substring(11,16));
        holder.pType.setText(event.getIcategory());
        holder.pDescription.setText(event.getIdiscription());
    }

    @Override
    public int getItemCount() {
        return mEvent.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout pLayout;
        TextView pDate;
        TextView pTime;
        TextView pType;
        TextView pDescription;
        //ImageView pImage;

        public MyViewHolder(View itemView) {

            super(itemView);
            pLayout = itemView.findViewById(R.id.long_pic_item);
            pDate = itemView.findViewById(R.id.item_date);
            pTime = itemView.findViewById(R.id.item_time);
            pType = itemView.findViewById(R.id.item_type);
            pDescription = itemView.findViewById(R.id.item_description);
        }
    }
}