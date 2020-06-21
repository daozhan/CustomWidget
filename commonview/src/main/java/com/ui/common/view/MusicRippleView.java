package com.ui.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.ui.common.Utils;

/**
 * 仿网易云音乐的鲸云音效波纹扩散效果。
 *
 * @author mjzuo
 * @since 19/12/29
 */
public class MusicRippleView extends View {

    private static final int MSG_DRAW0 = 0;
    private static final int MSG_DRAW1 = 1;

    // 波纹属性
    private Paint mRPaint0;
    private Paint mRPaint1;
    private int mRMaxRadius, mRMinRadius;
    private int mCurRadius0;
    private int mCurRadius1;
    private int mStrokeWidth;

    // 波纹上圆圈
    private Paint mCirclePaint0;
    private Paint mCirclePaint1;
    private int radius;
    private int curRadius0;
    private int curRadius1;
    private int curIndex0;
    private int curIndex1;
    // 圆心1
    private PointF circlePointF0;
    // 圆心2
    private PointF circlePointF1;
    private float cirAngel0;
    private float cirAngel1;

    // 每次扩散间隔
    private int distance;
    // 动画间隔时间
    private int animaBotIntervalTime = 400;

    private MusicRippleView.DrawTimingThread drawTimingThread;

    public MusicRippleView(Context context) {
        this(context, null);
    }

    public MusicRippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicRippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * 开始动画
     */
    public void startAnima() {
        if (drawTimingThread != null) {

            drawTimingThread.sendEmptyMessage(MSG_DRAW0);
            // 先取整，再取中
            float time = (mRMaxRadius - mRMinRadius) / distance * 0.5f;
            drawTimingThread.sendEmptyMessageDelayed(MSG_DRAW1
                    , (int)(animaBotIntervalTime * time));
        }
    }

    /**
     * 结束动画
     */
    public void stopAnima() {
        if (drawTimingThread != null) {
            drawTimingThread.removeMessages(MSG_DRAW0);
            drawTimingThread.removeMessages(MSG_DRAW1);
        }

        mCurRadius0 = mRMinRadius;
        mCurRadius1 = mRMinRadius;
        circlePointF0 = null;
        circlePointF1 = null;

        invalidate();
    }

    private void init() {
        drawTimingThread = new MusicRippleView
                .DrawTimingThread(Looper.getMainLooper());

        mRMaxRadius = Utils.dip2px(getContext(), 80);
        mRMinRadius = Utils.dip2px(getContext(), 40);
        mCurRadius0 = mRMinRadius;
        mCurRadius1 = mRMinRadius;
        mStrokeWidth = Utils.dip2px(getContext(), 1);
        distance = Utils.dip2px(getContext(), 5);

        radius = Utils.dip2px(getContext(), 5);
        curRadius0 = radius;

        mRPaint0 = new Paint();
        mRPaint0.setColor(0xffedc263);
        mRPaint0.setStyle(Paint.Style.STROKE);
        mRPaint0.setStrokeWidth(mStrokeWidth);
        mRPaint0.setAntiAlias(true);
        mRPaint0.setAlpha(255);

        mRPaint1 = new Paint();
        mRPaint1.setColor(0xffedc263);
        mRPaint1.setStyle(Paint.Style.STROKE);
        mRPaint1.setStrokeWidth(mStrokeWidth);
        mRPaint1.setAntiAlias(true);
        mRPaint1.setAlpha(255);

        mCirclePaint0 = new Paint();
        mCirclePaint0.setAntiAlias(true);
        mCirclePaint0.setStyle(Paint.Style.FILL);
        mCirclePaint0.setColor(0xffd96b63);

        mCirclePaint1 = new Paint();
        mCirclePaint1.setAntiAlias(true);
        mCirclePaint1.setStyle(Paint.Style.FILL);
        mCirclePaint1.setColor(0xff6cbe89);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthModel) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                widthSize = mRMaxRadius * 2 + getPaddingLeft()
                        + getPaddingRight() + 2 * mStrokeWidth;
                break;

            case MeasureSpec.EXACTLY:
                break;
        }

        switch (heightModel) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                heightSize = mRMaxRadius * 2 + getPaddingTop()
                        + getPaddingBottom() + 2 * mStrokeWidth;
                break;

            case MeasureSpec.EXACTLY:
                break;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mRMaxRadius + mStrokeWidth + getPaddingLeft()
                , mRMaxRadius + mStrokeWidth + getPaddingBottom()
                , mCurRadius0
                , mRPaint0);

        canvas.drawCircle(mRMaxRadius + mStrokeWidth + getPaddingLeft()
                , mRMaxRadius + mStrokeWidth + getPaddingBottom()
                , mCurRadius1
                , mRPaint1);

        if (circlePointF0 != null) {
            canvas.drawCircle(circlePointF0.x
                    , circlePointF0.y
                    , curRadius0
                    , mCirclePaint0);
        }

        if (circlePointF1 != null) {
            canvas.drawCircle(circlePointF1.x
                    , circlePointF1.y
                    , curRadius1
                    , mCirclePaint1);
        }
    }

    private class DrawTimingThread extends Handler {

        public DrawTimingThread(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try {
                switch (msg.what){
                    case MSG_DRAW0:
                        if (mCurRadius0 <= mRMaxRadius) {
                            mCurRadius0 += distance;
                        } else {
                            mCurRadius0 = mRMinRadius + distance;
                        }

                        circlePointF0 = drawCircleOnRipple(MSG_DRAW0, curIndex0);

                        mRPaint0.setAlpha(getAlphaOfRipple(curIndex0));//透明度
                        mCirclePaint0.setAlpha(getAlphaOfRipple(curIndex0));
                        curRadius0 = getRadiusOnRipple(curIndex0);

                        curIndex0 ++;
                        if (curIndex0 > (mRMaxRadius - mRMinRadius) / distance)
                            curIndex0 = 0;

                        cancleHandle(MSG_DRAW0);

                        break;
                    case MSG_DRAW1:

                        if (mCurRadius1 <= mRMaxRadius) {
                            mCurRadius1 += distance;
                        } else {
                            mCurRadius1 = mRMinRadius + distance;
                        }

                        circlePointF1 = drawCircleOnRipple(MSG_DRAW1, curIndex1);

                        mRPaint1.setAlpha(getAlphaOfRipple(curIndex1));
                        mCirclePaint1.setAlpha(getAlphaOfRipple(curIndex1));
                        curRadius1 = getRadiusOnRipple(curIndex1);

                        curIndex1 ++;
                        if (curIndex1 > (mRMaxRadius - mRMinRadius) / distance)
                            curIndex1 = 0;

                        cancleHandle(MSG_DRAW1);

                        break;
                }
            } catch (Exception e) {
                Log.e(">>tag1234", "MusicRippleView view error :" + msg);
            }
        }
    }

    private PointF drawCircleOnRipple(int msg, int index) {
        //周期开始，随机初始角度
        if (index == 0)
            if (msg == MSG_DRAW0)
                cirAngel0 = (float) (Math.random() * - 360 + 180);
            else
                cirAngel1 = (float) (Math.random() * - 360 + 180);

        PointF progressPoint = Utils
                .calcArcEndPointXY(mRMaxRadius + getPaddingLeft() + mStrokeWidth
                        , mRMaxRadius + getPaddingTop() + mStrokeWidth
                        , msg == MSG_DRAW0 ? mCurRadius0 : mCurRadius1
                        //每个周期旋转45度
                        , (msg == MSG_DRAW0 ? curIndex0 : curIndex1)
                                * 45f / ((mRMaxRadius - mRMinRadius) * 1.0f / distance)
                        , msg == MSG_DRAW0 ? cirAngel0 : cirAngel1);

        return progressPoint;
    }

    private void cancleHandle(int msg) {
        drawTimingThread.removeMessages(msg);
        drawTimingThread.sendEmptyMessageDelayed(msg, animaBotIntervalTime);

        invalidate();
    }

    /**
     * 透明度。
     */
    private int getAlphaOfRipple(int curIndex) {
        // 只取150的二进制
        int alpha = curIndex * 150 * distance / (mRMaxRadius - mRMinRadius);
        return 255 - alpha;
    }

    /**
     * 环绕圆比例。
     */
    private int getRadiusOnRipple(int curIndex) {
        return (int)(radius - curIndex * radius * 0.4f * distance / (mRMaxRadius - mRMinRadius));
    }
}
