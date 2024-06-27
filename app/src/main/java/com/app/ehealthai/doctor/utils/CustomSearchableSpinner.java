package com.app.ehealthai.doctor.utils;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.app.ehealthai.doctor.utils.SearchableSpinner;

public class CustomSearchableSpinner extends SearchableSpinner {

    public static boolean isSpinnerDialogOpen = false;

    public CustomSearchableSpinner(Context context) {
        super(context);
    }

    public CustomSearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!isSpinnerDialogOpen) {
                isSpinnerDialogOpen = true;
                return super.onTouch(v, event);
            }
            isSpinnerDialogOpen = false;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isSpinnerDialogOpen = false;
            }
        }, 500);
        return true;
    }
}