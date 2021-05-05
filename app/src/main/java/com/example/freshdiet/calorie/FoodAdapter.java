package com.example.freshdiet.calorie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshdiet.R;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    int type;
    public ArrayList<FoodListItem> FoodDatas;
    public ArrayList<FoodInfo> FoodDatas2;


    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
    private FoodAdapter.OnItemClickListener mListener=null;
    public void setOnItemClickListener(FoodAdapter.OnItemClickListener listener){
        this.mListener=listener;
    }

    public FoodAdapter(int num, ArrayList<FoodListItem> list){
        this.type=num;
        this.FoodDatas=list;
    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(type==1){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.food_popup_recyler_item, parent, false);
        }
        else if(type==2) {

        }

        FoodAdapter.FoodViewHolder holder = new FoodAdapter.FoodViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodListItem data = FoodDatas.get(position);

        holder.description.setText(data.getTitle());
        holder.description2.setText(data.getDesc());
    }

    @Override
    public int getItemCount() {
        return FoodDatas.size();
    }

    public FoodListItem getItem(int pos){
        return FoodDatas.get(pos);
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        public TextView description;
        public TextView description2;
        private Button cancelbtn;

        public FoodViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.foodname);
            description2=(TextView)itemView.findViewById(R.id.foodamount);
            cancelbtn=itemView.findViewById(R.id.fcancel_btn);

            cancelbtn.setOnClickListener(new View.OnClickListener(){

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
