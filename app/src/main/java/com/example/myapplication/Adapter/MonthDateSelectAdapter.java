package com.example.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Bean.DayBean;
import com.example.myapplication.Bean.MonthBean;
import com.example.myapplication.R;

import java.util.List;

/**
 * 描述：
 * 时间：2019/4/22
 * 作者：ztn
 */
public class MonthDateSelectAdapter extends RecyclerView.Adapter<MonthDateSelectAdapter.Holder> {

    private Context context;
    private List<MonthBean> list;
    private List<DayBean> currList;
    private List<DayBean> nextList;
    private onDateSelectListener listener;
    private com.example.myapplication.Adapter.DayDateSelectAdapter adapter,adapter1;

    public MonthDateSelectAdapter(Context context, List<MonthBean> list, List<DayBean> currList, List<DayBean> nextList) {
        this.context = context;
        this.list = list;
        this.currList = currList;
        this.nextList = nextList;
    }


    public interface onDateSelectListener{
        void onSelect(int pos, int month);
    }

    public void setListener(onDateSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dateselect_month, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        if (position==0) {
            RecyclerView mRecyclerView = holder.mRecyclerView;
            mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 7));
            adapter = new com.example.myapplication.Adapter.DayDateSelectAdapter(context,currList);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setHasFixedSize(true);

            adapter.setListener(new com.example.myapplication.Adapter.DayDateSelectAdapter.onDateSelectListener() {
                @Override
                public void onSelect(int pos) {
                    if(listener!=null) {
                        listener.onSelect(pos,holder.getAdapterPosition());
                    }
                }
            });
        } else if (position==1) {
            RecyclerView mRecyclerView = holder.mRecyclerView;
            mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 7));
            adapter1 = new com.example.myapplication.Adapter.DayDateSelectAdapter(context,nextList);
            mRecyclerView.setAdapter(adapter1);
            mRecyclerView.setHasFixedSize(true);

            adapter1.setListener(new com.example.myapplication.Adapter.DayDateSelectAdapter.onDateSelectListener() {
                @Override
                public void onSelect(int pos) {
                    if(listener!=null) {
                        listener.onSelect(pos,holder.getAdapterPosition());
                    }
                }
            });
        }

        MonthBean entity = list.get(position);
        holder.tv_year_month.setText(entity.getYear() + "-" + entity.getMonth());
    }

    public void notifySelectChanage(){
        adapter.notifyDataSetChanged();
        adapter1.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tv_year_month;
        RecyclerView mRecyclerView;
        Holder(View itemView) {
            super(itemView);
            tv_year_month = (TextView) itemView.findViewById(R.id.tv_year_month);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.mRecyclerView);
        }
    }

}
