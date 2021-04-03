package com.example.freshdiet.plan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.freshdiet.Popup2;

import java.util.ArrayList;

public class ClockView extends View{
    private final Context context;
    private int height, width, radius;
    private int padding=0, fontSize=0;
    private int numeralSpacing=0;
    private boolean isInit;
    private Paint paint;
    Paint npaint;
    private int[] numbers={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
    private Rect rect=new Rect();
    private RectF rectF=new RectF();
    float[] offset=new float[25];


    public ClockView(Context context) {
        super(context);
        this.context=context;
        this.setWillNotDraw(false);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.setWillNotDraw(false);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        this.setWillNotDraw(false);
    }



    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.GRAY);
        drawCircle(canvas);
        drawCenter(canvas);
        drawNumeral(canvas);
        drawSector(canvas);
        postInvalidateDelayed(500);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x,y, centerX=width/2, centerY=height/2;
        float distance;
        int r=radius+padding-80;
        // 화면에 터치가 발생했을 때 호출되는 콜백 메서드
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_MOVE :
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                distance= (float) Math.sqrt((x-centerX)*(x-centerX)+(y-centerY)*(y-centerY));
                if(distance>r) break;
                else {
                    checkInside(x,y,distance);

                }

        }


        return true;
    }

    private void initClock() {
        height = getHeight();
        width = getWidth();
        padding = numeralSpacing + 80;
        // 안드로이드에서 다양한 화면크기의 기기들을 지원하려면 고정 단위인 px 보다 밀도에 따른 논리 단위인 dip 를 사용해야한다.
        // 안드로이드 레이아웃 코드를 작성할 때 DP는 사용할 수 X --> PX로 바꾸어줘야 한다.
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13,
                getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        paint = new Paint(); // Paint 선언 --> 도화지+색연필 준비
        npaint=new Paint();
        isInit = true;

        for(int i=0;i<24;i++){
            offset[i]=(270+15*i)%360.0f;
        }
    }


    private void drawCircle(Canvas canvas){

        if (!isInit) {
            initClock();
        }
        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.white)); // 펜의 색깔
        paint.setStrokeWidth(5); // 펜의 두께
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); // antialias 적용 안하면 계단 현상, 적용하면 부드러워짐
        canvas.drawCircle(width / 2, height / 2, radius + padding - 80, paint);

    }

    private void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, 12, paint);
    }


    private void drawNumeral(Canvas canvas){
        paint.setTextSize(fontSize);

        int nradius=radius+padding-25;

        for(int number:numbers){
            String tmp=String.valueOf(number);
            paint.getTextBounds(tmp,0,tmp.length(),rect);
            double angle=Math.PI/12*(number-6);
            int x = (int) (width / 2 + Math.cos(angle) * nradius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * nradius + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawSector(Canvas canvas){
        for(int i = 0; i< MakePlan.timeArray.size(); i++) {
            String[] temp = MakePlan.timeArray.get(i).split(":");
            int length = temp.length;
            fillCircle(canvas, Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]), Integer.parseInt(temp[2]) * 60 + Integer.parseInt(temp[3]), Integer.parseInt(temp[length - 1]), temp[4]);
        }

        for(int i=0;i<MakePlan.timeArray2.size();i++) {
            String[] temp = MakePlan.timeArray2.get(i).split(":");
            int length = temp.length;
            fillCircle(canvas, Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]), Integer.parseInt(temp[2]) * 60 + Integer.parseInt(temp[3]), Integer.parseInt(temp[length - 1]), temp[4]);
        }

    }


    // 시간 받아서 원 채우기
    private void fillCircle(Canvas canvas, int start, int end, int color, String text){

        int starthour=start/60, startmin=start%60;
        int endhour=end/60, endmin=end%60;

        float distance=0.0f;
        if(endhour<starthour){
            end=(endhour+24)*60+endmin;
        }
        distance=end-start;
        float angle= 0.0f;

        int curpos=starthour%24; // 현재 위치(시간, 1시면 배열의 1번 인덱스)
        float startAngle=0.0f, endAngle=0.0f;

        startAngle=offset[curpos]+startmin*0.25f;
        angle=distance*0.25f;
        endAngle=startAngle+angle;
        rectF.set(width/2-(radius+padding-80), height/2-(radius+padding-80), width/2+(radius+padding-80), height/2+(radius+padding-80));

        npaint.setColor(color);
        npaint.setStyle(Paint.Style.FILL);

        canvas.drawArc(rectF, startAngle, angle, true, npaint);


        float medianAngle=(startAngle+(angle/2f))*(float)Math.PI/180f;
        npaint.setTextAlign(Paint.Align.CENTER);
        npaint.setTextSize(40);
        npaint.setColor(Color.BLACK);
        float rotateAngle=0.0f;
        canvas.save();

        if (startAngle >= 90 && startAngle <=180) {
            rotateAngle = 180 + startAngle;
        } else if (startAngle >= 180 && startAngle <= 270) {
            rotateAngle = startAngle- 180;
        } else rotateAngle = startAngle;

        canvas.rotate(rotateAngle,(float)(width/2 + (radius * 0.5*Math.cos(medianAngle))), (float)(height/2 + (radius *0.5* Math.sin(medianAngle))));
        canvas.drawText(text,(float)(width/2 + (radius * 0.5*Math.cos(medianAngle))), (float)(height/2 + (radius *0.5* Math.sin(medianAngle))), npaint);
        canvas.restore();
    }



    private void checkInside(float x, float y, float dist){
        float centerX=width/2, centerY=height/2;
        float sinO=(y-centerY)/dist;
        float ceta=((float) Math.toDegrees(Math.asin(sinO))+360.0f)%360.0f;

        if(x<centerX){
           ceta=(540-ceta)%360.0f;
        }

        if(checkArray(MakePlan.timeArray, ceta)) return;

        checkArray(MakePlan.timeArray2, ceta);
    }

    private boolean checkArray(ArrayList<String> curlist, float ceta){
        int length=curlist.size();
        for(int i=0;i<length;i++){
            String t=curlist.get(i);
            Log.i("ee",t);
            String[] temp = curlist.get(i).split(":");
            int templength=temp.length;

            int starthour=Integer.parseInt(temp[0]), startmin=Integer.parseInt(temp[1]);
            int endhour=Integer.parseInt(temp[2]), endmin=Integer.parseInt(temp[3]);
            int start=starthour*60+startmin, end=endhour*60+endmin;

            float distance=0.0f;
            if(endhour<starthour){
                end=(endhour+24)*60+endmin;
            }
            distance=end-start;
            float angle= 0.0f;

            int curpos=starthour%24; // 현재 위치(시간, 1시면 배열의 1번 인덱스)
            float startAngle=0.0f, endAngle=0.0f;

            startAngle=offset[curpos]+startmin*0.25f;
            angle=distance*0.25f;
            endAngle=startAngle+angle;

            if(startAngle>endAngle){
                if(ceta>=startAngle&&ceta<360 || ceta>=0&&ceta<=endAngle){
                    if(templength==7)
                        makePopup(i,1);
                    else if(templength==8)
                        makePopup(i,2);
                    return true;
                }
            }
            else{
                if(ceta>=startAngle&&ceta<=endAngle){
                    if(templength==7)
                        makePopup(i,1);
                    else if(templength==8)
                        makePopup(i,2);
                    return true;
                }
            }
        }
        return false;
    }

    private void makePopup(int index, int type){
        String[] temp;
        Intent intent=new Intent(context, Popup2.class);

        if(type==1){
            temp = MakePlan.timeArray.get(index).split(":");
            intent.putExtra("Memo",temp[5]);
        }
        else {
            temp = MakePlan.timeArray2.get(index).split(":");
            intent.putExtra("Type",temp[5]);
            intent.putExtra("Memo",temp[6]);
        }

        intent.putExtra("Todo", temp[4]);
        intent.putExtra("TodoIndex",index);

        context.startActivity(intent);

    }

}
