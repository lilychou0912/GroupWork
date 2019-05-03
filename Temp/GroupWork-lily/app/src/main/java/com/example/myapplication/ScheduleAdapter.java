package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.text.ParseException;
import java.util.Calendar;
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

//import com.bumptech.glide.Glide;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {
    private List<Schedule> mSchedule;
    private DBhelper dBhelper = new DBhelper();
    private Context context;
    private String sStarttime, sEndtime;
    private OnitemClick onitemClick;   //定义点击事件接口
    private OnLongClick onLongClick;  //定义长按事件接口

    //定义设置点击事件监听的方法
    public void setOnitemClickLintener (OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
    //定义设置长按事件监听的方法
    public void setOnLongClickListener (OnLongClick onLongClick) {
        this.onLongClick = onLongClick;
    }

    //定义一个点击事件的接口
    public interface OnitemClick {
        void onItemClick(int position);
    }
    //定义一个长按事件的接口
    public interface OnLongClick {
        void onLongClick(int position);
    }

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
    public void onBindViewHolder(final MyViewHolder holder,  final int position) {
        Schedule sche = mSchedule.get(position);
        Glide.with (context).load (sche.getImgUrl ()).into (holder.sImage);
        holder.sName.setText(sche.getName());
        holder.sDuration.setText(sche.getstarttime()+"~"+sche.getendtime());
        sStarttime = sche.getstarttime();
        sEndtime = sche.getendtime();

        /**长按事件监听，长按开启修改页面
        holder.sCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String name = holder.todoItem.getText().toString();
                Intent intent = new Intent(v.getContext(), New_ToDo_Activity.class);
                intent.putExtra("name_extra", name);
                v.getContext().startActivity(intent);
                return true;
            }
        });
         **/
        //点击卡片进入详细编辑页面
        holder.sCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Schedule Sche = mSchedule.get(position);
                String name = holder.sName.getText().toString();
                String starttime = Sche.getstarttime() ;
                String endtime = Sche.getendtime();
                int sId = Sche.getSId();
                Intent intent = new Intent(v.getContext(), NoteEditActivity.class);
                intent.putExtra("sId_extra", sId);
                intent.putExtra("name_extra", name);
                intent.putExtra("starttime_extra",starttime);
                intent.putExtra("endtime_extra",endtime);
                v.getContext().startActivity(intent);

            }
            });


        /**
        //生成长图
        holder.sPicture.setOnClickListener(new View.OnClickListener() {
            private View picView;
            @Override
            public void onClick(View v) {
                LayoutInflater factorys = LayoutInflater.from(.this);
                final View textEntryView = factorys.inflate(R.layout.edit_note, null);
                picView = (View) textEntryView.findViewById(R.id.控件ID);
            }
        });
**/
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
                        mSchedule.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());

                        dBhelper.deleteSchedule(name);
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

    /*生成长图的方法*/
    public static Bitmap getViewDrawingCacheBitmap(View view) {
        view = view.getRootView();
        if (!view.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bm = view.getDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bm;
    }

    /*加入水印（ first:生成的长图，second:水印）
    public static Bitmap add2Bitmap(Bitmap first, Bitmap second) {
        float scale = ((float) first.getWidth()) / second.getWidth();
        second = ImageUtil.scaleImg(second, scale);
        int width = first.getWidth();
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }*/

    @Override
    public int getItemCount() {
        return mSchedule.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sLayout;
        ImageView sImage;
        TextView sName;
        TextView sDuration;
        TextView sPublish;
        TextView sPicture;
        TextView sDelete;
        CardView sCardView;

        public MyViewHolder(View itemView) {

            super(itemView);
            sLayout = itemView.findViewById(R.id.s_layout);
            sImage = itemView.findViewById(R.id.s_pic);
            sName = itemView.findViewById(R.id.s_title);
            sDuration = itemView.findViewById(R.id.s_duration);
            sDelete = itemView.findViewById(R.id.s_delete);
            sCardView = itemView.findViewById(R.id.note_view);

        }
    }
}