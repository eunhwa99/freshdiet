package com.example.freshdiet.calorie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.freshdiet.R;

import java.util.ArrayList;

public  class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
    private OnItemClickListener mListener=null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }

    public ArrayList<HorizontalData> HorizontalDatas;

    public void setData(ArrayList<HorizontalData> list){
        HorizontalDatas = list;
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizon_recycler_item, parent, false);

        HorizontalViewHolder holder = new HorizontalViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position) {
        HorizontalData data = HorizontalDatas.get(position);

        holder.description.setText(data.getText());
    }

    @Override
    public int getItemCount() {
        return HorizontalDatas.size();
    }

    public HorizontalData getItem(int position){
        return HorizontalDatas.get(position);
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder {

        public TextView description;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
              description = (TextView) itemView.findViewById(R.id.horizon_description);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        //       HorizontalData data=HorizontalDatas.get(pos);
                        if(mListener!=null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }
    }
}



