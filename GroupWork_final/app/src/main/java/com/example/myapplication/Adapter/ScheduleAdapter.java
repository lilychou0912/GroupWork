package com.example.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Bean.Schedule;
import com.example.myapplication.R;
import com.example.myapplication.ui.NoteEditActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

//import com.bumptech.glide.Glide;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {
    private List<Schedule> mSchedule = new ArrayList<>();
    //private DBhelper dBhelper = new DBhelper();
    private Context context;
    private String sStarttime, sEndtime;
    private OnItemLongClickListener mOnItemLongClickListener;//定义长按事件接口

    //定义设置长按事件监听的方法
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    //定义一个长按事件的接口
    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    public ScheduleAdapter(Context context, List<Schedule> list) {
        this.context = context;
        this.mSchedule = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("adapter1", mSchedule.get(0).getName());
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.schedule_item, parent,
                false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,  final int position) {
        Log.d("adapter2", mSchedule.get(0).getName());
        Schedule sche = mSchedule.get(position);
        holder.sName.setText(sche.getName());
        holder.sDuration.setText(sche.getstarttime()+"~"+sche.getendtime());
        sStarttime = sche.getstarttime();
        sEndtime = sche.getendtime();

        if (position == (mSchedule.size() - 1)) {
            holder.sBlank.setVisibility(View.VISIBLE);
        } else {
            holder.sBlank.setVisibility(View.GONE);
        }

        //长按事件监听，长按开启修改页面
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }


        /**
        holder.sCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String name = holder.sName.getText().toString();
                NoteOverviewActivity note = new NoteOverviewActivity();
                note.showPopupWindow();
                note.onClick(v);
                return true;
            }
        });
**/
        //点击卡片进入详细编辑页面
        holder.sCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bmob初始化
                Bmob.initialize(context, "1768baa1a8670ebb020369f5672e443f" );
                Schedule Sche = mSchedule.get(position);
                String name = holder.sName.getText().toString();
                String starttime = Sche.getstarttime() ;
                String endtime = Sche.getendtime();
                String sId = Sche.getObjectId();
                Intent intent = new Intent(v.getContext(), NoteEditActivity.class);
                intent.putExtra("sId_extra", sId);
                intent.putExtra("name_extra", name);
                intent.putExtra("starttime_extra",starttime);
                intent.putExtra("endtime_extra",endtime);
                v.getContext().startActivity(intent);

            }
            });

        //删除按钮
        holder.sDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("提示");
                alertDialogBuilder
                        .setMessage("你确定要删除整个游记吗？");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = holder.sName.getText().toString();
                        String objectId = mSchedule.get(holder.getAdapterPosition()).getObjectId();
                        mSchedule.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());

                        Schedule schedule = new Schedule();
                        schedule.setObjectId(objectId);
                        schedule.remove("author");
                        schedule.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.e("yeaB", "yeah");
                                } else {
                                    Log.e("BMOB", e.toString());
                                    //Snackbar.make(mFabAddPost, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });

                        //删除动画
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                });
                alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {

        //Log.d("adapter3", mSchedule.get(0).getName());
        if (mSchedule == null) {
            return 0;
        }
        return mSchedule.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sLayout;
        TextView sName;
        TextView sDuration;
        TextView sDelete;
        CardView sCardView;
        LinearLayout sBlank; //底部空白

        public MyViewHolder(View itemView) {

            super(itemView);
            sLayout = itemView.findViewById(R.id.s_layout);
            sName = itemView.findViewById(R.id.s_title);
            sDuration = itemView.findViewById(R.id.s_duration);
            sDelete = itemView.findViewById(R.id.s_delete);
            sCardView = itemView.findViewById(R.id.note_view);
            sBlank = itemView.findViewById(R.id.blank_layout);
        }
    }
}