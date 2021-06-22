package com.pjhank.web;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class WaveView1 extends View {
    Paint paint,borderpaint,mPaint, mPaintMore;;

    final int water_color = Color.parseColor("#28A9FC");

    private Path mPath;//Route
    private PointF drawPoint, drawPoint2;//Draw point
    private ValueAnimator animator, animatorh;

    private Double anglenum = Math.PI / 180;
    private int count = 0;//Plotting
    private int mWaveHeight;
    private int mShowWaveHe;
    private float arcRa = 0;//Circle radius
    private float mWidth, mHeight, waveHeight;
    private float waveDeep = 8f;
    private float waveDeepmin = 8f;//Minimum
    private float waveDeepMax = 20f;//Maximum
    private Boolean iscircle = true;//



    public WaveView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        paint = new Paint();
        paint.setColor(water_color);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (getWidth() / 2)+15;
        int yPos = (getHeight() / 2);
        float borders = arcRa*1.1f;
        if (iscircle) {
            mPath.reset();
            mPath.addCircle(xPos, yPos, arcRa, Path.Direction.CW);//radius
            canvas.clipPath(mPath);
            //System.out.println(arcRa);
        }
        canvas.drawText(String.valueOf(mShowWaveHe)+"%", xPos-60, yPos+25, borderpaint);
        //canvas.drawCircle(xPos, yPos, 210, borderpaint);
        drawPoint.x = 0;
        Double rightperiod = Math.PI / 8 * count;
        if (count == 16){
            count = 0;
        }else {
            count++;
        }
        while (drawPoint.x < mWidth) {
            drawPoint.y = (float) (waveHeight - waveDeep * Math.sin(drawPoint.x * anglenum - rightperiod));
            //drawPoint2.y = (float) (waveHeight - waveDeep * Math.sin(drawPoint.x * anglenum - rightperiod - Math.PI / 2));
            canvas.drawLine(drawPoint.x, drawPoint.y, drawPoint.x, mHeight, mPaint);
            drawPoint.x++;

        }
        //canvas.drawCircle(xPos, yPos, 115, borderpaint);
        postInvalidateDelayed(25);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;//Get  width
        mHeight = h;//Get height
        if (mWidth > mHeight) {
            arcRa = (mHeight / 2)-35;
            if (iscircle) {
                mWidth = mHeight;
            }
        }else {
            arcRa = (mWidth / 2)-35;
            if (iscircle) {
                mHeight = mWidth;
            }
        }
        waveHeight = mHeight;
        ChangeWaveLevel(mWaveHeight);
        super.onSizeChanged(w, h, oldw, oldh);
    }
    private void init() {
        borderpaint = new Paint();
        borderpaint.setStrokeWidth(8f);
        //borderpaint.setColor(Color.BLACK);
        borderpaint.setStyle(Paint.Style.FILL);
        borderpaint.setAntiAlias(true);
        borderpaint.setTextSize(80);

        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(water_color);//Set color
        //mPaint.setAntiAlias(antiAlias);//Anti-Aliasing(Performance impact)
        mPaint.setStyle(Paint.Style.FILL);
        //mPaint.setAlpha(100);
        //mPaintMore = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mPaintMore.setAntiAlias(antiAlias);//Anti-Aliasing
        //mPaintMore.setStyle(Paint.Style.FILL);
        //mPaintMore.setColor(nextColor);//Set color
        //mPaintMore.setAlpha(30);
        drawPoint = new PointF(0, 0);
        drawPoint2 = new PointF(0, 0);

    }

    public void setValues(int WaveHeight) {
        mWaveHeight = WaveHeight/10;
        mShowWaveHe = WaveHeight;

    }


    public void ChangeWaveLevel(int percent) {
        animator = ValueAnimator.ofFloat(waveDeepmin, waveDeepMax);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                waveDeep = (float) animation.getAnimatedValue();
            }
        });
        animator.setRepeatMode(ValueAnimator.REVERSE);//Round
        animator.setRepeatCount(1);
        animatorh = ValueAnimator.ofFloat(waveHeight, mHeight * (10.5f - percent) / 10);//water level
        animatorh.setDuration(5000);
        animatorh.setInterpolator(new DecelerateInterpolator());
        animatorh.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                waveHeight = (float) animation.getAnimatedValue();
            }
        });
        animator.start();//Start animation
        animatorh.start();//Start animation
        System.out.println(percent);

        }


}
