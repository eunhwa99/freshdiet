package com.example.freshdiet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

public class ClockView extends View {
    private int height, width, radius;
    private int padding=0, fontSize=0;
    private int numeralSpacing=0;
    private boolean isInit;
    private Paint paint;
    private int[] numbers={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
    private Rect rect=new Rect();
    private RectF rectF=new RectF();
    float[] offset=new float[25];


    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas){

        canvas.drawColor(Color.GRAY);
        //getTime=MakePlan.timeArray;
        drawCircle(canvas);
        drawCenter(canvas);
        drawNumeral(canvas);
        for(int i=0;i<MakePlan.timeArray.size();i++) {
            String[] temp = MakePlan.timeArray.get(i).split(":");
            int length = temp.length;
            fillCircle(canvas, Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]), Integer.parseInt(temp[2]) * 60 + Integer.parseInt(temp[3]), Integer.parseInt(temp[length - 1]), temp[4]);
        }

        postInvalidateDelayed(500);
        invalidate();
    }

    private void initClock() {
        height = getHeight();
        width = getWidth();
        padding = numeralSpacing + 50;
        // 안드로이드에서 다양한 화면크기의 기기들을 지원하려면 고정 단위인 px 보다 밀도에 따른 논리 단위인 dip 를 사용해야한다.
        // 안드로이드 레이아웃 코드를 작성할 때 DP는 사용할 수 X --> PX로 바꾸어줘야 한다.
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13,
                getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        paint = new Paint(); // Paint 선언 --> 도화지+색연필 준비
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

        for(int number:numbers){
            String tmp=String.valueOf(number);
            paint.getTextBounds(tmp,0,tmp.length(),rect);
            double angle=Math.PI/12*(number-6);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }


    // 시간 받아서 원 채우기
    private void fillCircle(Canvas canvas, int start, int end, int color, String text){

        Paint npaint=new Paint();
        int starthour=start/60, startmin=start%60;
        int endhour=end/60, endmin=end%60;

        float distance=0.0f;
        if(endhour<starthour){
            end=(endhour+24)*60+endmin;
        }
        distance=end-start;
        float angle= 0.0f;

        int curpos=starthour%24; // 현재 위치(시간, 1시면 배열의 1번 인덱스)
        float startAngle=0.0f;

        startAngle=offset[curpos]+startmin*0.25f;
        angle=distance*0.25f;
        rectF.set(width/2-(radius+padding-80), height/2-(radius+padding-80), width/2+(radius+padding-80), height/2+(radius+padding-80));

        npaint.setColor(color);
        npaint.setStyle(Paint.Style.FILL);
        npaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawArc(rectF, startAngle, angle, true, npaint);


        float medianAngle=(startAngle+(angle/2f))*(float)Math.PI/180f;
        npaint.setTextAlign(Paint.Align.CENTER);
        npaint.setTextSize(30);
        npaint.setColor(Color.WHITE);
        canvas.drawText(text,(float)(width/2 + (radius * 0.5*Math.cos(medianAngle))), (float)(height/2 + (radius *0.5* Math.sin(medianAngle))), npaint);

    }

}
