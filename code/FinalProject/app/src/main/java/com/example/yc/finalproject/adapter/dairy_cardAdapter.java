package com.example.yc.finalproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yc.finalproject.R;
import com.example.yc.finalproject.model.dairy_user;

import java.util.List;

public class dairy_cardAdapter extends RecyclerView.Adapter<dairy_cardAdapter.ViewHolder>{

    private List<dairy_user> cardList;
    private OnItemClickListener mOnItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        TextView Time;
        TextView Topic;
        TextView Content;

        public ViewHolder(View view) {
            super(view);
            cardView = view;
            Time = (TextView) view.findViewById(R.id.time);
            Topic = (TextView) view.findViewById(R.id.topic);
            Content = (TextView) view.findViewById(R.id.content);
        }
    }

    public dairy_cardAdapter(List<dairy_user> CardList) {
        cardList = CardList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dairy_recycler_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                dairy_user usrs = cardList.get(position);
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
    public void onBindViewHolder(final dairy_cardAdapter.ViewHolder holder, final int position) {
        dairy_user users = cardList.get(position);
        holder.Time.setText(users.getTime());
        holder.Topic.setText(users.getTopic());
        holder.Content.setText(users.getContent());

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