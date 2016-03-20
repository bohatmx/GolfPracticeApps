package com.boha.golfpractice.library.util;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.golfpractice.library.R;

/**
 * Created by aubreymalabie on 3/19/16.
 */
public class HoleCounter extends CardView{

    public interface HoleCounterListener {
        void onHoleNumberChanged(int holeNumber);
    }

    HoleCounterListener listener;
    ImageView iconPrevious, iconNext;
    TextView txtNumber;
    int currentNumber = 1;
    static final int NUMBER_OF_HOLES = 18;

    public HoleCounter(Context context) {
        super(context);
        initializeView();
    }

    public HoleCounter(Context context, AttributeSet attrs) {
        super(context,attrs);
        initializeView();
    }

    private void initializeView() {
        View v = inflate(getContext(), R.layout.hole_counter, null);
        iconPrevious = (ImageView) v.findViewById(R.id.iconPrev);
        iconNext = (ImageView) v.findViewById(R.id.iconNext);
        txtNumber = (TextView) v.findViewById(R.id.holeNumber);
        txtNumber.setText(""+currentNumber);

        View prev = v.findViewById(R.id.prevLayout);
        View next = v.findViewById(R.id.nextLayout);

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentNumber++;
                if (currentNumber > NUMBER_OF_HOLES) {
                    currentNumber = 1;

                }
                txtNumber.setText(""+currentNumber);
                listener.onHoleNumberChanged(currentNumber);
            }
        });
        prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentNumber--;
                if (currentNumber < 1) {
                    currentNumber = 1;

                }
                txtNumber.setText(""+currentNumber);
                listener.onHoleNumberChanged(currentNumber);
            }
        });

        addView(v);
    }

    public void setListener(HoleCounterListener listener) {
        this.listener = listener;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }
}
