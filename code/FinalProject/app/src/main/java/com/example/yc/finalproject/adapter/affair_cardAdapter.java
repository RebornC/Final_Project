package com.example.yc.finalproject.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.model.affair_user;

import java.util.List;

public class affair_cardAdapter extends RecyclerView.Adapter<affair_cardAdapter.ViewHolder>{

    private List<affair_user> cardList;
    private OnItemClickListener mOnItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        TextView Time;
        TextView Affair;
        TextView Deadline;
        CardView Body;
        CheckBox finish;

        public ViewHolder(View view) {
            super(view);
            cardView = view;
            Time = (TextView) view.findViewById(R.id.time);
            Affair = (TextView) view.findViewById(R.id.affair);
            Deadline = (TextView) view.findViewById(R.id.deadline);
            Body = (CardView) view.findViewById(R.id.body);
            finish = (CheckBox) view.findViewById(R.id.finish);
        }
    }

    public affair_cardAdapter(List<affair_user> CardList) {
        cardList = CardList;
    }

    public void updateData(List<affair_user> CardList) {
        this.cardList = CardList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.affair_recycler_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                affair_user usrs = cardList.get(position);
            }
        });
        return holder;
    }

    public interface OnItemClickListener {//回调接口
        void onClick(int position);//单击
        void onLongClick(int position);//长按
    }

     //定义这个接口的set方法，便于调用

    public void setOnClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final affair_cardAdapter.ViewHolder holder, final int position) {
        affair_user users = cardList.get(position);
        holder.Time.setText(users.getTime());
        holder.Affair.setText(users.getAffair());
        holder.Deadline.setText("Deadline："+users.getDeadline());
        if (users.getFinish().equals("1")) {
            holder.Body.setBackgroundColor(Color.parseColor("#93FF93"));
            holder.finish.setChecked(true);
        } else {
            holder.Body.setBackgroundColor(Color.parseColor("#FF9797"));
            holder.finish.setChecked(false);
        }

        holder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                affair_user users = cardList.get(position);
                if (users.getFinish().equals("0")) {
                    holder.Body.setBackgroundColor(Color.parseColor("#93FF93"));
                    holder.finish.setChecked(true);
                    users.setFinish("1");
                } else {
                    holder.Body.setBackgroundColor(Color.parseColor("#FF9797"));
                    holder.finish.setChecked(false);
                    users.setFinish("0");
                }
            }
        });

        //设置点击和长按事件
        if (mOnItemClickListener != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


}