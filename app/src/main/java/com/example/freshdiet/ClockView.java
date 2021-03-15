package com.example.freshdiet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private int[] numbers={1,2,3,4,5,6,7,8,9,10,11,12};
    private Rect rect=new Rect();
    private RectF rectF=new RectF();


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
            String[] temp=MakePlan.timeArray.get(i).split(":");
            int length=temp.length;
            for(int j=0;j<length;j++) {
                fillCircle(canvas, Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]), Integer.parseInt(temp[2]) * 60 + Integer.parseInt(temp[3]), Integer.parseInt(temp[length-1]));
            }

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
           // Log.i("위치", String.valueOf(rect.left)+" "+ String.valueOf(rect.right)+" "+String.valueOf(rect.top));
            double angle=Math.PI/6*(number-3);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    // 시간 받아서 원 채우기
    private void fillCircle(Canvas canvas, int start, int end, int color){
        int starthour=start/60, startmin=start%60;

        Log.i("시간2", starthour+" "+end);
        float distance=Math.abs(end-start);
        float angle= 0.0f, offset=0.0f;
        switch(starthour){
            case 0:
                offset= 270.0f;
                break;
            case 1:
                offset= 300.0f;
                break;
            case 2:
                offset=330.0f;
                break;
            case 3:
                offset=0.0f;
                break;
            case 4:
                offset=30.0f;
                break;
            case 5:
                offset=60.0f;
                break;
            case 6:
                offset=90.0f;
                break;
            case 7:
                offset=120.0f;
                break;
            case 8:
                offset=150.0f;
                break;
            case 9:
                offset=180.0f;
                break;
            case 10:
                offset=210.0f;
                break;
            case 11:
                offset=240.0f;
                break;
        }
        offset=offset+startmin*0.5f;
        angle=distance*0.5f;
        rectF.set(width/2-(radius+padding-80), height/2-(radius+padding-80), width/2+(radius+padding-80), height/2+(radius+padding-80));

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF, offset, angle, true, paint);
    }

}
