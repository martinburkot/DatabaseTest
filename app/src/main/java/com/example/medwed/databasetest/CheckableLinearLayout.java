package com.example.medwed.databasetest;

/**
 * Created by medwed on 4/21/2016.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;


public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private Checkable _checkbox;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // find checked text view
        int childCount = getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View v = getChildAt(i);
            if (v instanceof Checkable) {
                _checkbox = (Checkable)v;
            }
        }
    }

    @Override
    public boolean isChecked() {
        return _checkbox != null ? _checkbox.isChecked() : false;
    }

    @Override
    public void setChecked(boolean checked) {
        if (_checkbox != null) {
            _checkbox.setChecked(checked);
        }
    }

    @Override
    public void toggle() {
        if (_checkbox != null) {
            _checkbox.toggle();
        }
    }
}
