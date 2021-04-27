package com.example.freshdiet.calorie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.freshdiet.R;

import java.util.ArrayList;

public class FoodListViewAdapter extends BaseAdapter implements Filterable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList. (원본 데이터 리스트)
    private ArrayList<FoodListItem> listViewItemList=new ArrayList<>();
    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 보유.
    private ArrayList<FoodListItem> filteredItemList = listViewItemList ;

    Filter listFilter ;

    // ListViewAdapter의 생성자
    public FoodListViewAdapter(ArrayList<FoodListItem> list) {
        listViewItemList=list;

    }

    @Override
    public int getCount() {
        return filteredItemList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.food_listview_item, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
        FoodListItem listViewItem = filteredItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());

        return convertView;
    }


    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem( String title, String desc) {
        FoodListItem item = new FoodListItem();

       // item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        listViewItemList.add(item);
    }

    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter() ;
        }

        return listFilter ;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            ArrayList<FoodListItem> itemList = new ArrayList<FoodListItem>();
            if (constraint == null || constraint.length() == 0) {
                results.values = itemList ;
                results.count = 0 ;

            } else {

                    for (FoodListItem item : listViewItemList) {
                        if (item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getDesc().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            itemList.add(item);
                        }
                    }

                    results.values = itemList;
                    results.count = itemList.size();
                }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // update listview by filtered data list.
            filteredItemList = (ArrayList<FoodListItem>) results.values ;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }

}
