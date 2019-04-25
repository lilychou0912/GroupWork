package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptC;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {
    private List<Schedule> mSchedule;
    private DBhelper dBhelper = new DBhelper();
    private Context context;

    public ScheduleAdapter(Context context, List<Schedule> list) {
        this.context = context;
        this.mSchedule = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.schedule_item, parent,
                false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Schedule sche = mSchedule.get(position);
        Glide.with (context).load (sche.getImgUrl ()).into (holder.sImage);
        holder.sName.setText(sche.getName());
        holder.sDuration.setText(sche.getstarttime()+"-"+sche.getendtime());
        //长按事件监听，长按开启修改页面

        //删除按钮
        holder.sDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = holder.sName.getText().toString();
                mSchedule.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

                dBhelper.deleteSchedule(name);
                //删除动画
                notifyItemRemoved(position);
                notifyDataSetChanged();
    }
});
    }

    @Override
    public int getItemCount() {
        return mSchedule.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sLayout;
        ImageView sImage;
        TextView sName;
        TextView sDuration;
        TextView sDelete;

        public MyViewHolder(View itemView) {

            super(itemView);
            sLayout = itemView.findViewById(R.id.s_layout);
            sImage = itemView.findViewById(R.id.s_pic);
            sName = itemView.findViewById(R.id.s_title);
            sDuration = itemView.findViewById(R.id.s_duration);
            sDelete = itemView.findViewById(R.id.s_delete);
        }
    }


}
