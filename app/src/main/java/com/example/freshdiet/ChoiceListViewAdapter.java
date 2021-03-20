package com.example.freshdiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChoiceListViewAdapter extends BaseAdapter {

    public ChoiceListViewAdapter() {

    }
    private ArrayList<String> listViewItemList=new ArrayList<>();

    @Override
    public int getCount() {
        return listViewItemList.size(); //Adapter에 사용되는 데이터 개수 리턴
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 View 리턴
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos=i;
        final Context context=viewGroup.getContext();

        // "plannerlist" layout을 inflate 하여 convertView 참조 획득
        if(view==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.plannerlist2, viewGroup, false);
        }

        TextView nameText=(TextView)view.findViewById(R.id.nameText2);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        String listViewItem = listViewItemList.get(pos);
        nameText.setText(listViewItem);
        return view;
    }

    public void addItem(String name){
        listViewItemList.add(name);
    }
    public void removeItem(int i){
        listViewItemList.remove(i);
    }


}
