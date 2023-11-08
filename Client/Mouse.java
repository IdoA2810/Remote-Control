package com.example.remoteclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class Mouse extends View
{
    Paint paint;
    float xPos;
    float yPos;
    float xDist;
    float yDist;
    boolean changed;
    public Mouse(Context context)
    {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        changed = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(xPos, yPos, 10, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            xDist = event.getX() - xPos;
            yDist = event.getY() - yPos;
            changed = true;
        }

        xPos = event.getX();
        yPos = event.getY();

        invalidate();
        return true;
    }
}
