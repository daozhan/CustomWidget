package com.ui.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ui.common.R;
import com.ui.common.Utils;

/**
 * 圆环刻度VIEW。
 *
 * @author mjzuo
 * @since 18/04/28
 */
public class RingScaleView extends View {

    private static final String STATE_EXCELLENT = "优";
    private static final String STATE_GOOD = "良";
    private static final String STATE_MILD = "轻度污染";
    private static final String STATE_MIDDLE = "中度污染";
    private static final String STATE_SEVERE = "重度污染";

    private static final String STATE_EXCELLENT_STR = "无健康影响";
    private static final String STATE_GOOD_STR = "无健康影响";
    private static final String STATE_MILD_STR = "有轻微影响";
    private static final String STATE_MIDDLE_STR = "危害健康";
    private static final String STATE_SEVERE_STR = "严重危害健康";

    /**
     * 这是每段的颜色值，用来渐变显示进度
     */
    private static final int[] STATE_CORLOR = new int[]
            {0xFF00FF00, 0xFF3299CC, 0xFFD9D919,
             0xFFff7f00, 0xFFFF2400, 0xFFFF0000};

    /**
     * 圆环内部的背景及文字属性
     */
    int ringBgCorlor;
    int slideRingCorlor;
    int radius;
    int wordCorlor;
    int wordSize;

    /**
     * 进度条属性
     */
    int maxProgress;
    int minProgress;
    // 当前进度
    int progress;
    // 开始滑动的起始位置度数，左侧180
    int beginLocation;
    int realShowProgress;

    /**
     * 外围文字属性
     */
    int outWordSize;
    int outWordCorlor;
    String state = "";
    int wordWith;
    int wordHeigh;

    /**
     * 内部文字属性
     */
    String inputWordStr;
    int inputWordSize;
    int inputWordCorlor;
    int inputWorldSpace;
    String currentInputWorldStr;

    // 外侧刻度线的数量
    int scaleLineCount;
    // 外侧正常刻度线的长度
    int scaleLineLength;
    // 线条的宽度
    int scaleLineWidth;
    // 需要特殊处理的刻度线长度，例如特殊方位上的刻度或者当前刻度
    int specialScaleLineLength;
    // 刻度结束的角度，这里为整圆
    float sweepAngle = 360;
    // 未选择的刻度线颜色
    int scaleLineNormalCorlor;
    // 进度不为0时的刻度线颜色
    int[] specialScaleCorlors;
    // 起始位置的线颜色
    int beginLineCorlor;
    // 刻度线距离里面的环的距离
    int scaleToRingSpace;

    Paint scalePaint;
    Paint specialScalePaint;
    Paint RingPaint;
    Paint CircleBgPaint;
    Paint wordPaint;
    Paint inputWordPaint;
    Rect rect;

    public RingScaleView(Context context) {
        this(context, null);
    }

    public RingScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 设置当前的展示刻度。
     */
    public void setProgress(int p) {
        if (p <= maxProgress && p >= minProgress) {
            realShowProgress = p;
            progress = (int)((realShowProgress - minProgress)
                    * 100.0 / (maxProgress - minProgress));
            // 圈外文字
            state = getState(progress);
            // 圈内文字
            currentInputWorldStr = getStateStr(progress);
        }

        invalidate();
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs
                , R.styleable.RingScaleView
                , defStyleAttr, 0);

        /**
         * 内部圆及圆弧的属性
         */
        slideRingCorlor = array.getInt(R.styleable.RingScaleView_slideRingCorlor
                , 0x80c0c0c0);
        ringBgCorlor = array.getInt(R.styleable.RingScaleView_ringBgCorlor
                , 0x806cbe89);
        radius = array.getInt(R.styleable.RingScaleView_radius
                , Utils.dip2px(context, 64));
        wordCorlor = array.getInt(R.styleable.RingScaleView_wordCorlor
                , Color.BLUE);
        wordSize = array.getInt(R.styleable.RingScaleView_wordSize
                , 18);

        /**
         * 刻度线的属性
         */
        scaleLineCount = array.getInt(R.styleable.RingScaleView_scaleLineCount
                , 100);
        scaleLineLength = array.getInt(R.styleable.RingScaleView_scaleLineLength
                , Utils.dip2px(context, 10));
        specialScaleLineLength = array.getInt(R.styleable.RingScaleView_specialScaleLineLength
                , Utils.dip2px(context, 15));
        scaleToRingSpace = array.getInt(R.styleable.RingScaleView_scaleToRingSpace
                , Utils.dip2px(context, 0));
        scaleLineNormalCorlor = array.getInt(R.styleable.RingScaleView_scaleLineNormalCorlor
                , 0xFFbebebe);
        scaleLineWidth = array.getInt(R.styleable.RingScaleView_scaleLineWidth
                , Utils.dip2px(context, 4));
        beginLineCorlor = array.getInt(R.styleable.RingScaleView_beginLineNormalCorlor
                , 0xFF00009C);
        specialScaleCorlors = STATE_CORLOR;

        /**
         * 外围文字属性
         */
        outWordCorlor = array.getInt(R.styleable.RingScaleView_outWordColor
                , 0xFF3399FF);
        outWordSize = array.getInt(R.styleable.RingScaleView_outWordSize
                , Utils.sp2px(context, 12));
        wordWith = array.getInt(R.styleable.RingScaleView_outWordWidth
                , Utils.sp2px(context, 50));
        wordHeigh = array.getInt(R.styleable.RingScaleView_outWordHeight
                , Utils.sp2px(context, 20));

        /**
         * 内部文字的属性
         */
        inputWordSize = array.getInt(R.styleable.RingScaleView_inputWordSize
                , Utils.sp2px(context, 14));
        inputWordCorlor = array.getInt(R.styleable.RingScaleView_inputWordColor
                , 0xFFffffff);
        inputWorldSpace = array.getInt(R.styleable.RingScaleView_inputWordSpace
                , Utils.sp2px(context, 5));

        /**
         * 刻度值
         */
        maxProgress = array.getInt(R.styleable.RingScaleView_maxProgress
                , 100);
        minProgress = array.getInt(R.styleable.RingScaleView_minProgress
                , 0);
        progress = array.getInt(R.styleable.RingScaleView_progress
                , 0);

        array.recycle();
    }

    private void initPaint() {
        /**
         * 普通刻度线
         */
        scalePaint = new Paint();
        scalePaint.setColor(scaleLineNormalCorlor);
        scalePaint.setAntiAlias(true);
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setStrokeWidth(scaleLineWidth);

        /**
         * 带颜色的刻度线
         */
        specialScalePaint = new Paint();
        specialScalePaint.setAntiAlias(true);
        specialScalePaint.setStyle(Paint.Style.STROKE);
        specialScalePaint.setStrokeWidth(scaleLineWidth);

        /**
         * 背景圆
         */
        CircleBgPaint = new Paint();
        CircleBgPaint.setColor(ringBgCorlor);
        CircleBgPaint.setAntiAlias(true); // 抗锯齿效果
        CircleBgPaint.setStyle(Paint.Style.FILL); //设置空心
        CircleBgPaint.setStrokeCap(Paint.Cap.ROUND); // 圆形笔头

        /**
         * 走过的圆弧
         */
        RingPaint = new Paint();
        RingPaint.setAntiAlias(true);
        RingPaint.setStyle(Paint.Style.FILL);
        RingPaint.setColor(slideRingCorlor);
        RingPaint.setStrokeCap(Paint.Cap.ROUND); // 圆形笔头

        /**
         * 圈外提示字
         */
        wordPaint = new Paint();
        wordPaint.setColor(outWordCorlor);
        wordPaint.setTextSize(outWordSize);
        wordPaint.setTypeface(Typeface.DEFAULT_BOLD);
        rect = new Rect();

        /**
         * 圈内提示字
         */
        inputWordPaint = new Paint();
        inputWordPaint.setColor(inputWordCorlor);
        inputWordPaint.setTextSize(inputWordSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthModel) {
            // 当宽度自适应的时候
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                /**
                 * 宽度 = 圆的直径 + 左内边距 + 右内边距
                 * + 左右两侧刻度线的长度 + 刻度线距离圆的间隔
                 * + 两侧外围字的宽度
                 */
                widthSize = 2 * radius + getPaddingLeft() + getPaddingRight()
                        + 2 * (specialScaleLineLength+scaleToRingSpace)
                        + 2 * wordWith;
                break;

            // 当宽度全屏或者固定尺寸时候
            case MeasureSpec.EXACTLY:
                break;

        }
        switch (heightModel) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                // 高度计算与宽度计算方法同理
                heightSize = 2 * radius + getPaddingTop() + getPaddingBottom()
                        + 2 * (specialScaleLineLength+scaleToRingSpace)
                        + 2 * wordHeigh;
                break;

            case MeasureSpec.EXACTLY:
                break;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int curLeft = getPaddingLeft()
                + specialScaleLineLength
                + scaleToRingSpace
                + wordWith;
        final int curTop = getPaddingTop()
                + specialScaleLineLength
                + scaleToRingSpace
                + wordHeigh;
        final int curRight = 2 * radius
                + getPaddingLeft()
                + specialScaleLineLength
                + scaleToRingSpace
                + wordWith;
        final int cusBottom = 2 * radius
                + getPaddingTop()
                + specialScaleLineLength
                + scaleToRingSpace
                + wordHeigh;

        /**
         * 背景圆
         */
        canvas.drawArc(new RectF(curLeft, curTop, curRight, cusBottom)
                , beginLocation
                , 360
                , false
                , CircleBgPaint);

        /**
         * 走过圆弧
         */
        canvas.drawArc(new RectF(curLeft, curTop, curRight, cusBottom)
                , -90
                , progress * (360 / 100f)
                , true, RingPaint);

        /**
         * 刻度线
         */
        paintScale(canvas, curLeft, curTop);

        /**
         * 外围文字
         */
        paintOutWord(canvas, state, curLeft, curTop);

        /**
         * 内部文字
         */
        if (currentInputWorldStr != null) {
            inputWordPaint.getTextBounds(currentInputWorldStr
                    , 0
                    , currentInputWorldStr.length()
                    , rect);

            canvas.drawText(currentInputWorldStr
                    , radius + curLeft - (int)(rect.width() * 0.5)
                    , radius + curTop + (int)(rect.height() * 0.5  - inputWorldSpace)
                    , inputWordPaint);
        }
    }

    /**
     * 绘制刻度
     * @param canvas
     */
    private void paintScale(Canvas canvas, int curLeft, int curTop) {

        canvas.save();
        // 将坐标系移到圆中心
        canvas.translate(radius + curLeft,radius + curTop);
        // 旋转坐标系
        canvas.rotate(180);

        for (int i = 0; i < scaleLineCount + 1; i ++) {
            // 刻度线的实际展示长度
            int scaleLine = scaleLineLength;
            int color = scaleLineNormalCorlor;
            // 起始点颜色不同
            if (i == 0 || i == maxProgress) {
                color = beginLineCorlor;
            }
            // 每一段的颜色也不同
            else if (i / 20 < specialScaleCorlors.length) {
                color = Utils.evaluateColor(i, specialScaleCorlors);
            }
            // 特殊方位的线长短
            if (i % (20 / (100 / scaleLineCount)) == 0) {
                scaleLine = specialScaleLineLength;
            }
            specialScalePaint.setColor(color);

            // 画已经滑动过的刻度线,因为实际刻度数量都是按着100个来转换的
            if (i <= progress) {
                canvas.drawLine(0
                        ,radius + scaleToRingSpace
                        ,0,radius + scaleToRingSpace + scaleLine
                        ,specialScalePaint);
            }
            // 画未滑动到的刻度线
            else if (i > progress && i < maxProgress) {
                canvas.drawLine(0
                        ,radius + scaleToRingSpace
                        ,0,radius + scaleToRingSpace + scaleLine
                        ,scalePaint);
            }

            canvas.rotate(sweepAngle/(scaleLineCount * 1f));
        }

        //操作完成后恢复状态
        canvas.restore();
    }

    /**
     * 绘制外围文字。
     */
    private void paintOutWord(Canvas canvas, String state, int curLeft, int curTop) {
        PointF progressPoint = Utils.calcArcEndPointXY
                (radius + curLeft
                        , radius + curTop
                        , radius + specialScaleLineLength + scaleToRingSpace
                        , progress * (360 / 100f), -90);

        int left = (int) progressPoint.x;
        int top = (int) progressPoint.y;
        wordPaint.getTextBounds(state, 0, state.length(), rect);

        if (left < radius + curLeft) {
            left -= rect.width();
        }
        if (top > radius + curTop) {
            top += rect.height();
        }

        canvas.drawText(state, left, top, wordPaint);
    }

    /**
     * 根据刻度获取状态的方法。
     */
    private String getState(int progress) {
        switch (progress/20) {
            case 0:
                return STATE_EXCELLENT;
            case 1:
                return STATE_GOOD;
            case 2:
                return STATE_MILD;
            case 3:
                return STATE_MIDDLE;
            case 4:
                return STATE_SEVERE;
        }
        return "";
    }

    /**
     * 根据刻度获取状态描述的方法。
     */
    private String getStateStr(int progress) {
        switch (progress/20) {
            case 0:
                return STATE_EXCELLENT_STR;
            case 1:
                return STATE_GOOD_STR;
            case 2:
                return STATE_MILD_STR;
            case 3:
                return STATE_MIDDLE_STR;
            case 4:
                return STATE_SEVERE_STR;
        }
        return null;
    }

}
