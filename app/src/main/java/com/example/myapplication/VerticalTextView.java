package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

public class VerticalTextView extends AppCompatTextView {
    final boolean side;

    public VerticalTextView( Context context,
                             AttributeSet attrs )
    {
        super( context, attrs );
        final int gravity = getGravity();
        if (( gravity & Gravity.VERTICAL_GRAVITY_MASK )
                == Gravity.BOTTOM  && Gravity.isVertical(gravity))
        {
            setGravity(
                    ( gravity & Gravity.HORIZONTAL_GRAVITY_MASK )
                            | Gravity.TOP );
            side = false;
        }
        else
        {
            side = true;
        }
    }

    @Override
    protected void onMeasure( int widthMeasureSpec,
                              int heightMeasureSpec )
    {
        super.onMeasure( heightMeasureSpec,
                widthMeasureSpec );
        setMeasuredDimension( getMeasuredHeight(),
                getMeasuredWidth() );
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        TextPaint tPaint = getPaint();
        tPaint.setColor( getCurrentTextColor() );
        tPaint.drawableState = getDrawableState();

        canvas.save();

        if ( side )
        {
            canvas.translate( getWidth(), 0 );
            canvas.rotate( 90 );    //в нашем компоненте при android:gravity="bottom|right"
        }
        else
        {
            canvas.translate( 0, getHeight() );
            canvas.rotate( -90 );   //в нашем компоненте при android:gravity="top|right"
        }

        canvas.translate( getCompoundPaddingLeft(),
                getExtendedPaddingTop() );

        getLayout().draw( canvas );
        canvas.restore();
    }
}
