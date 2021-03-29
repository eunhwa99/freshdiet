package com.example.freshdiet.calorie;

import android.os.AsyncTask;
import android.util.Log;

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
    @Override
    protected String doInBackground(String... strings) {

        try {
           // serviceKey= URLDecoder.decode(serviceKey,"UTF-8");
            requestUrl="http://openapi.foodsafetykorea.go.kr/api/"+serviceKey+"/I2790/xml/6/21";
            URL url = new URL(requestUrl);
            InputStream is = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            ((XmlPullParser) parser).setInput(new InputStreamReader(is, "UTF-8"));
            String tag;
            int eventType = parser.getEventType();
            String preName=null;
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<String>();
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.END_TAG:
                        preName=null;
                        break;
                    case XmlPullParser.START_TAG:
                        preName = parser.getName();
                        break;
                    case XmlPullParser.TEXT:
                        if(preName==null)  //혹시 preName이 null일수 있으니 처리
                        {

                        }

                        else if(parser.getText().equals("DESC_KOR") ) {
                           // list.add(parser.getText());
                            Log.d("이름: ",parser.getText());
                        }
                        break;
                }
                eventType = parser.next();
            }
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
