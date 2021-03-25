package com.example.freshdiet.plan;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.freshdiet.R;

public class ListViewCheck extends LinearLayout implements Checkable {
    public ListViewCheck(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * boolean isCheck() : 현재 Checked 상태를 리턴.
     * void setChecked(boolean checked) : Checked 상태를 checked 변수대로 설정.
     * void toggle() : 현재 Checked 상태를 바꿈. (UI에 반영)
     */
    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        return cb.isChecked() ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        setChecked(cb.isChecked() ? false : true);
    }
}
