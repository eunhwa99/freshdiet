package com.example.freshdiet.calorie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.freshdiet.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class FoodMain extends Fragment {
    ViewGroup rootView;
    FoodListViewAdapter adapter;
    ListView listview = null ;
    ArrayList<FoodListItem> mainList=new ArrayList<>();;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.foodmain, container, false);

       XMLTask1 xmltask=new XMLTask1();
        xmltask.execute();

        listview = (ListView)rootView.findViewById(R.id.food_listview);
        listview.setAdapter(adapter);
        getFoodList();
        setTextChanged(); // 검색 기능

        return rootView;
    }
    private void getFoodList(){
     //   mainList=new ArrayList<>();
        FoodListItem item=new FoodListItem();
        item.setTitle("고기");
        item.setDesc("황제");
        mainList.add(item);
    }

    private void setTextChanged(){
        EditText editTextFilter = (EditText)rootView.findViewById(R.id.editTextFilter) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                ((FoodListViewAdapter)listview.getAdapter()).getFilter().filter(filterText) ;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;
    }

    public class XMLTask1 extends AsyncTask<String,Void, String> {
        private String requestUrl;
        private String serviceKey="3b0058815936482daa41";
        // "http://openapi.foodsafetykorea.go.kr/api/3b0058815936482daa41/I2790/xml/1/5 "
        @Override
        protected String doInBackground(String... strings) {

            requestUrl="http://openapi.foodsafetykorea.go.kr/api/"+serviceKey+"/I2790/xml/6/21";
            try {
                URL url = new URL(requestUrl);

                InputStream is = url.openStream();
               // Toast.makeText(FoodMain.this, is.toString());
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                ((XmlPullParser) parser).setInput(new InputStreamReader(is, "UTF-8"));
                boolean f_name=false;
                boolean f_maker=false;
                int eventType = parser.getEventType();
                String preName=null;
                FoodListItem item = null;
                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            item=new FoodListItem();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("DESC_KOR")) {
                                mainList.add(item);
                            }
                            preName=null;
                            break;
                        case XmlPullParser.START_TAG:
                            preName = parser.getName();
                            if(preName.equals("DESC_KOR")){
                                f_name=true;
                            }
                            else if(preName.equals("MAKER_NAME")){
                                f_maker=true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if(preName==null)  //혹시 preName이 null일수 있으니 처리
                            {

                            } else if(f_name) {
                                item.setTitle(parser.getText());

                            }else if(f_maker){
                                item.setDesc(parser.getText());
                            }
                            break;

                    }
                    eventType = parser.next();
                }
               // FoodMain food=new FoodMain();
              //  food.mainList=arrayList;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            adapter=new FoodListViewAdapter(mainList);

            // 리스트뷰 참조 및 Adapter달기
            listview.setAdapter(adapter);
            //어답터 연결
        }
    }

}
