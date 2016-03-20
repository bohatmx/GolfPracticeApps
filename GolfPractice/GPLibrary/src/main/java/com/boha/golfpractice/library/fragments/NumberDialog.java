package com.boha.golfpractice.library.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.boha.golfpractice.library.R;

import java.lang.reflect.Field;

/**
 * Created by aubreymalabie on 3/19/16.
 */
public class NumberDialog extends DialogFragment {

    TextView txtTitle;
    Button btnDone, btnCancel;
    NumberPicker numberPicker;
    View view;
    String title;
    int type;
    Context ctx;
    static final int DISTANCE_TO_PIN = 1,
    PUTT_LENGTH = 2, NUMBER_OF_PUTTS = 3, SCORE = 4;
    public interface NumberDialogListener {
        void onNumberSelected(int number);
    }

    NumberDialogListener numberDialogListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        view = inflater.inflate(R.layout.number_dialog, container, false);
        ctx = inflater.getContext();
        getDialog().setTitle(title);
        setFields();
        return view;
    }

    private void setFields() {
        txtTitle = (TextView) view.findViewById(R.id.title);
        btnDone = (Button) view.findViewById(R.id.btnDone);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);

        txtTitle.setText(title);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberDialogListener.onNumberSelected(numberPicker.getValue());
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setNumberPickerTextSize(32);
        switch (type) {
            case DISTANCE_TO_PIN:
                numberPicker.setMaxValue(300);
                numberPicker.setMinValue(5);
                numberPicker.setValue(150);
                break;
            case PUTT_LENGTH:
                numberPicker.setMaxValue(30);
                numberPicker.setMinValue(1);
                numberPicker.setValue(1);
                break;
            case NUMBER_OF_PUTTS:
                numberPicker.setMaxValue(6);
                numberPicker.setMinValue(0);
                numberPicker.setValue(1);
                break;
            case SCORE:
                numberPicker.setMaxValue(10);
                numberPicker.setMinValue(1);
                numberPicker.setValue(1);
                break;
        }
    }

    public void setListener(NumberDialogListener numberDialogListener) {
        this.numberDialogListener = numberDialogListener;
    }
    public void setDialogTitle(String title) {
        this.title = title;
    }

    public void setNumberType(int type) {
        this.type = type;
    }

    public  boolean setNumberPickerTextSize(int size)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof TextView){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(ContextCompat.getColor(ctx, R.color.green_700));

                    ((TextView)child).setTextSize(Float.parseFloat(""+size));
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                }
                catch(IllegalAccessException e){
                }
                catch(IllegalArgumentException e){
                }
            }
        }
        return false;
    }
}
