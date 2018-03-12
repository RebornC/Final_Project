package com.example.yc.finalproject.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yc.finalproject.model.Memo;
import com.example.yc.finalproject.R;
import com.example.yc.finalproject.affairMemo.affair_MainActivity;
import com.example.yc.finalproject.birthMemo.birth_MainActivity;
import com.example.yc.finalproject.dairyMemo.dairy_login_activity;
import com.example.yc.finalproject.pawMeno.psw_login_activity;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder>{

    private List<Memo> mMemoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View memoView;
        TextView memoName;
        TextView memoIntro;
        ImageView imageId;

        public ViewHolder(View view) {
            super(view);
            memoView = view;
            memoName = (TextView) view.findViewById(R.id.memo_name);
            memoIntro = (TextView) view.findViewById(R.id.memo_intro);
            //imageId = (ImageView) view.findViewById(R.id.image_id);
        }
    }

    public MemoAdapter(List<Memo> memoList) {
        mMemoList = memoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.memoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Memo memo = mMemoList.get(position);
                if (memo.getMemo_name().equals("> > 生日备忘录")) {
                    Intent it = new Intent(v.getContext(), birth_MainActivity.class);
                    v.getContext().startActivity(it);
                } else if (memo.getMemo_name().equals("> > 密码管家")) {
                    Intent it = new Intent(v.getContext(), psw_login_activity.class);
                    v.getContext().startActivity(it);
                } else if (memo.getMemo_name().equals("> > 日志")) {
                    Intent it = new Intent(v.getContext(), dairy_login_activity.class);
                    v.getContext().startActivity(it);
                } else if (memo.getMemo_name().equals("> > 待办事项")) {
                    Intent it = new Intent(v.getContext(), affair_MainActivity.class);
                    v.getContext().startActivity(it);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Memo memo = mMemoList.get(position);
        holder.memoName.setText(memo.getMemo_name());
        holder.memoIntro.setText(memo.getMemo_intro());
        //holder.imageId.setImageResource(memo.getImage_id());
    }

    @Override
    public int getItemCount() {
        return mMemoList.size();
    }

}