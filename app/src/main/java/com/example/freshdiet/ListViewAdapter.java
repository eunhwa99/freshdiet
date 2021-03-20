package com.example.freshdiet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 arraylist
   // private ArrayList<ListViewItem> listViewItemList=new ArrayList<ListViewItem>();
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
            view=inflater.inflate(R.layout.plannerlist, viewGroup, false);
        }

        TextView nameText=(TextView)view.findViewById(R.id.nameText);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        String listViewItem = listViewItemList.get(pos);
        nameText.setText(listViewItem);
        return view;
    }

    public void addItem(String name){
        listViewItemList.add(name);
       // this.notifyDataSetChanged();
    }

    public void removeItem(int i){
        listViewItemList.remove(i);
    }

}
