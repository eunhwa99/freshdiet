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


    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private FoodAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(FoodAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public FoodAdapter(int num, Object list) {
        this.type = num;
        if (num == 1)
            this.FoodDatas = (ArrayList<FoodListItem>) list;
        else if (num == 2)
            this.FoodDatas2 = (ArrayList<FoodInfo>) list;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (type == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.food_popup_recyler_item, parent, false);
        } else if (type == 2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.food_info_recycler_item, parent, false);
        }

        FoodAdapter.FoodViewHolder holder = new FoodAdapter.FoodViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        if (type == 1) {
            FoodListItem data = FoodDatas.get(position);

            holder.description.setText(data.getTitle());
            holder.description2.setText(data.getDesc());
        } else if (type == 2) {
            FoodInfo data = FoodDatas2.get(position);

            holder.description.setText(data.name);
            holder.description2.setText(data.gram + data.unit);
            holder.description3.setText(data.kal + "kcal");
        }
    }

    @Override
    public int getItemCount() {
        if (type == 1)
            return FoodDatas.size();
        else return FoodDatas2.size();
    }


    class FoodViewHolder extends RecyclerView.ViewHolder {

        public TextView description;
        public TextView description2, description3;
        private Button cancelbtn;

        public FoodViewHolder(View itemView) {
            super(itemView);
            if (type == 1) {
                description = (TextView) itemView.findViewById(R.id.foodname);
                description2 = (TextView) itemView.findViewById(R.id.foodamount);
                cancelbtn = itemView.findViewById(R.id.fcancel_btn);

                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            if (mListener != null) {
                                mListener.onItemClick(v, pos);
                            }
                        }
                    }
                });
            } else if (type == 2) {
                description = itemView.findViewById(R.id.foodname2);
                description2 = itemView.findViewById(R.id.foodamount2);
                description3 = itemView.findViewById(R.id.foodkal);

                itemView.setOnClickListener(view -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, pos);
                        }
                    }
                });
            }

        }
    }
}


