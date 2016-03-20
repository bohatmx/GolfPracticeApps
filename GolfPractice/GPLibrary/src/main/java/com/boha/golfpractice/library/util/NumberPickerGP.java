package com.boha.golfpractice.library.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.boha.golfpractice.library.R;

/**
 * Created by aubreymalabie on 3/19/16.
 */
public class NumberPickerGP extends  NumberPicker{

    public NumberPickerGP(Context context) {
        super(context);
    }

    public NumberPickerGP(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerGP(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberPickerGP(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        if(view instanceof EditText){
            ((EditText) view).setTextSize(26);
            ((EditText) view).setTextColor(ContextCompat.getColor(getContext(), R.color.green_700));
        }
    }


}
