package com.example.freshdiet.calorie;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

//https://m.blog.naver.com/PostView.nhn?blogId=cosmosjs&logNo=221348945666&proxyReferer=https:%2F%2Fwww.google.co.kr%2F

public class XMLTask extends AsyncTask<String,Void, String> {
    private String requestUrl;
    private ArrayList<String> list;
    private String serviceKey="3b0058815936482daa41";
   // "http://openapi.foodsafetykorea.go.kr/api/3b0058815936482daa41/I2790/xml/1/5 "
    ArrayList<FoodListItem> arrayList=new ArrayList<>();
    @Override
    protected String doInBackground(String... strings) {

        try {
            requestUrl="http://openapi.foodsafetykorea.go.kr/api/"+serviceKey+"/I2790/xml/1/5";
            URL url = new URL(requestUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
           XmlPullParser parser = factory.newPullParser();
          //  parser.setInput(url.openStream(),null);
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
                        arrayList.add(item);
                        break;
                    case XmlPullParser.END_TAG:
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
                        parser.getText();
                        break;
                   
                }
                eventType = parser.next();
            }
         //   FoodMain food=new FoodMain();
          //  food.mainList=arrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //어답터 연결
    //    MyAdapter adapter = new MyAdapter(getApplicationContext(), list);
       // recyclerView.setAdapter(adapter);
    }
}
