package com.ui.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ui.common.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿网易云音乐的鲸云音效效果。
 *
 * <p>通过贝塞尔曲线来实现，效果很差，
 * 就当学习贝塞尔了吧。
 *
 * @author mjzuo
 * @since 19/12/26
 */
public class MusicBView extends View {

    private double b = 0.552284749831;

    /**
     * 外围贝塞尔曲线属性
     */
    Paint bPaint;
    Path bPath;
    int mRadius; // 四条曲线组成的圆半径
    PointF cp1, cp2; // 两个控制点
    PointF startP, endP; // 起始点
    int mStrokeWidth;

    boolean isFirst = true;

    /**
     * 四条贝塞尔12个点，4个重复点只存一次
     */
    List<PointF> points = new ArrayList<>();

    private Context mContext;

    public MusicBView(Context context) {
        this(context, null);
    }

    public MusicBView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicBView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        init();
        calculateCp();
    }

    /**
     * 绘制刷新
     */
    public void start() {
        isFirst = false;

        calculateDynamicCp();
        invalidate();
    }

    /**
     * 停止绘制
     */
    public void stop() {
        isFirst = true;

        calculateCp();
        invalidate();

        if(points != null)
            points.clear();
    }

    /**
     * 设置颜色
     * @param paintColor
     */
    public void setColor(int paintColor) {
        if(bPaint != null && paintColor != 0)
            bPaint.setColor(paintColor);
    }

    private void init() {
        mRadius = Utils.dip2px(mContext, 40);
        mStrokeWidth = Utils.dip2px(mContext, 2);

        bPaint = new Paint();
        bPaint.setColor(0xff868686);
        bPaint.setStyle(Paint.Style.STROKE);
        bPaint.setStrokeWidth(mStrokeWidth);
        bPaint.setAntiAlias(true);

        bPath = new Path();
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
                widthSize = mRadius * 2 + getPaddingLeft()
                        + getPaddingRight() + 2 * mStrokeWidth;
                break;

            case MeasureSpec.EXACTLY:
                break;
        }

        switch (heightModel) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                heightSize = mRadius * 2 + getPaddingTop()
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
        canvas.save();
        // 平移画布
        canvas.translate(mRadius + mStrokeWidth + getPaddingLeft()
                , mRadius + mStrokeWidth + getPaddingTop());

        if (isFirst) {
            /**
             * 旋转画布
             */
            for (int index = 0; index < 4; index++) {
                canvas.rotate(90f);

                bPath.moveTo(startP.x, startP.y);
                bPath.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, endP.x, endP.y);

                canvas.drawPath(bPath, bPaint);
                bPath.reset();
            }
        } else {

            /**
             * 8个控制点和4个起始点，不旋转
             */
            for (int index = 0; index < 4; index++) {
                if (index == 0)
                    bPath.moveTo(points.get(0).x, points.get(0).y);
                else
                    bPath.lineTo(points.get(index * 3).x, points.get(index * 3).y);

                bPath.cubicTo(points.get(index * 3 + 1).x, points.get(index * 3 + 1).y
                        , points.get(index * 3 + 2).x, points.get(index * 3 + 2).y
                        , index != 3 ? points.get(index * 3 + 3).x : points.get(0).x
                        , index != 3 ? points.get(index * 3 + 3).y : points.get(0).y);
            }

            canvas.drawPath(bPath, bPaint);
            bPath.reset();
        }

        canvas.restore();
    }

    private void calculateCp() {

        b = 0.552284749831;

        if (startP == null || endP == null) {
            startP = new PointF(0, - mRadius);
            endP = new PointF(mRadius, 0);
        }

        /**
         * 平移后的画布坐标，坐标(0,0)为圆心
         */
        cp1 = new PointF((float) (mRadius * b), - mRadius);
        cp2 = new PointF(mRadius, - (float) (mRadius * b));
    }

    private void calculateDynamicCp() {

        b = Math.random() * 0.44 + 0.55;

        /**
         * 平移后的画布坐标，坐标(0,0)为圆心
         */
        if(points != null && points.size() != 0)
            points.clear();

        points.add(new PointF((float) (Math.random() * - 20 + 10)
                , - mRadius - (float) (Math.random() * 20)));
        points.add(new PointF((float) (mRadius * b)
                , - mRadius - (float) (Math.random() * 20)));
        points.add(new PointF(mRadius + (float) (Math.random() * 20)
                , - mRadius - (float) (Math.random() * 10 + 10)));

        points.add(new PointF(mRadius + (float) (Math.random() * 10 + 10)
                , (float) (Math.random() * - 20 + 10)));
        points.add(new PointF(mRadius + (float) (Math.random() * 20)
                , (float) (Math.random() * 0.5 * mRadius * b + 0.5 *mRadius * b)));
        points.add(new PointF((float) (mRadius * b + 10)
                , mRadius + (float) (Math.random() * 20)));

        points.add(new PointF((float) (Math.random() * - 20 + 10)
                , mRadius + (float) (Math.random() * 20)));
        points.add(new PointF((float) (- mRadius * b)
                , mRadius + (float) (Math.random() * 20)));
        points.add(new PointF(- mRadius - (float) (Math.random() * 20)
                , (float) (mRadius * b)));

        points.add(new PointF(- mRadius - (float) (Math.random() * 10 + 10)
                , (float) (Math.random() * - 20 + 10)));
        points.add(new PointF(- mRadius - (float) (Math.random() * 20)
                , (float) (- mRadius * b)));
        points.add(new PointF((float) (- mRadius * b)
                , - mRadius - (float) (Math.random() * 20)));
    }
}
