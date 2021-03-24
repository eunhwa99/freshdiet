package com.example.freshdiet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeMain extends AppCompatActivity {
    ChallengeAdapter adapter;
    ListView listView;
    private ArrayList<String> listitem=new ArrayList<>(Arrays.asList("숙면","식습관","운동/건강","기타"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengemain);

        initListView();

    }

    private void initListView(){
        adapter=new ChallengeAdapter(listitem);

        // 리스트뷰 참조 및 Adpater 달기
        listView=(ListView)findViewById(R.id.challengelistview);
        listView.setAdapter(adapter);

        // listview에 클릭 이벤트 핸들러 정의
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
    private class ChallengeAdapter extends BaseAdapter{
        private ArrayList<String> items;
        public ChallengeAdapter(ArrayList<String> items){
            this.items=items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.challengelist, viewGroup,false);

                v.setMinimumHeight(viewGroup.getHeight()/getCount());
            }
            ImageView imageView = (ImageView)v.findViewById(R.id.categoryiv);
            if("숙면".equals(items.get(i))){
                imageView.setImageResource(R.drawable.ic_baseline_nightlight_round_24);
            }
            else if("운동/건강".equals(items.get(i))){
                imageView.setImageResource(R.drawable.ic_baseline_directions_run_24);
            }
            else if("식습관".equals(items.get(i))){
                imageView.setImageResource(R.drawable.ic_baseline_flatware_24);
            }
            else{
                imageView.setImageResource(R.drawable.ic_baseline_more_horiz_24);
            }

            TextView textView=(TextView)v.findViewById(R.id.categorytext);
            textView.setText(items.get(i));

            return v;
        }
    }
}
