package com.ui.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ui.common.R;
import com.ui.common.Utils;

/**
 * 画几何形状的VIEW。
 *
 * <p>总结下API使用方法。可参考博客：
 * https://blog.csdn.net/MingJieZuo/article/details/100625718
 *
 * @author mingjiezuo
 * @since 19/09/08
 */
public class GeometricFigureView extends View {

    public static final int LINE_TYPE = 0;
    public static final int RECT_TYPE = 1;
    public static final int CIRCLE_TYPE = 2;
    public static final int OVAL_TYPE = 3;
    public static final int ROUND_CIRCLE_TYPE = 4;
    public static final int ARC_TYPE = 5;
    public static final int MORE_FIGURE_TYPE = 6;
    public static final int FILL_CORLOR = 7;
    public static final int TEXT_CORLOR = 8;
    public static final int BITMAP_TYPE = 9;
    public static final int CLIP_TYPE = 10;
    public static final int STATE_TYPE = 11;
    public static final int TRANSLATE_TYPE = 12;

    /**
     * 当前画形状的type：
     * <ul>
     * <li>0：线
     * <li>1：矩形
     * <li>2：圆形
     * <li>3：椭圆
     * <li>4：圆角矩形
     * <li>5：扇形
     * <li>6：多边形
     * <li>7：填充颜色
     * <li>8：文本
     * <li>9: bitmap
     * <li>10: 裁剪
     * <li>11: 旋转
     * <li>12: 平移
     * </ul>
     */
    private int mDrawType;

    private int defStyleAttr;

    /**
     *  画线段的画笔
     */
    private Paint linePaint;

    /**
     *  矩形的rectF
     */
    private RectF rectF;

    /**
     *  矩形的画笔
     */
    private Paint rectPaint;

    /**
     *  圆形的画笔
     */
    private Paint circlePaint;

    /**
     *  圆角矩形的rectF
     */
    private RectF mRoundRectF;

    /**
     *  圆角矩形的画笔
     */
    private Paint mRoundRectFPaint;

    /**
     * 椭圆的外切rectF
     */
    private RectF mOvalRectF;

    /**
     * 椭圆的画笔
     */
    private Paint mOvalPaint;

    /**
     * 弧的rectF
     */
    private RectF mArcRectF;

    /**
     * 弧的画笔
     */
    private Paint mArcPaint;

    /**
     * 多边形的path
     */
    private Path mMoreFIgurePath;

    /**
     * 多边形画笔
     */
    private Paint mMoreFigurePaint;

    /**
     * 文本的画笔
     */
    private Paint textPaint;

    /**
     * 绘制图片的bitmap
     */
    private Bitmap mBitmap;

    /**
     * bitmap画笔
     */
    private Paint mBitmapPaint;

    /**
     * 裁剪的path
     */
    private Path mClipPath;

    /**
     * 裁剪画布里的矩形
     */
    private RectF cropRectF;
    private Paint cropPaint;

    /**
     * 裁剪的rectF
     */
    private RectF mClipRectF;

    private Context mContext;

    public GeometricFigureView(Context context) {
        this(context, null);
    }

    public GeometricFigureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeometricFigureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.defStyleAttr = defStyleAttr;
        initView(attrs);
        initPaint();
    }

    public void setDrawType(int drawType) {
        mDrawType = drawType;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mDrawType) {
            case LINE_TYPE:
                drawLine(canvas);
                break;
            case RECT_TYPE:
                drawRect(canvas);
                break;
            case CIRCLE_TYPE:
                drawCircle(canvas);
                break;
            case OVAL_TYPE:
                drawOval(canvas);
                break;
            case ROUND_CIRCLE_TYPE:
                drawRoundRect(canvas);
            case ARC_TYPE:
                drawArc(canvas);
                break;
            case MORE_FIGURE_TYPE:
                drawMoreFigure(canvas);
                break;
            case FILL_CORLOR:
                drawCorlor(canvas);
                break;
            case TEXT_CORLOR:
                drawText(canvas);
                break;
            case BITMAP_TYPE:
                drawBitmap(canvas);
                break;
            case CLIP_TYPE:
                // 锁定当前画布
                canvas.save();
                // 裁剪画布
                drawClipPathOnCanval(canvas);
                // 在裁剪的画布内画矩形
                drawRectInCropCanvas(canvas);
                // 恢复画布
                canvas.restore();
                break;
            case STATE_TYPE:
                // 锁定当前画布
                canvas.save();
                // 画线
                drawRotate(canvas);
                // 恢复画布
                canvas.restore();
                break;
            case TRANSLATE_TYPE:
                // 锁定当前画布
                canvas.save();
                // 挪动画布
                drawTranslate(canvas);
                // 画线
                drawRotate(canvas);
                // 恢复画布
                canvas.restore();
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 只是简单介绍api，就不做处理了
    }

    private void initView(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = mContext.getTheme().obtainStyledAttributes(attrs
                    , R.styleable.GeometricFigureView
                    , defStyleAttr, 0);
            mDrawType = array.getInt(R.styleable.GeometricFigureView_draw_type, 0);
            //记得使用完销毁
            array.recycle();
        }
    }

    private void initPaint() {
        // 画线
        linePaint = new Paint();
        linePaint.setColor(0xff868686);

        // 画矩形
        rectPaint = new Paint();
        rectPaint.setColor(0xff868686);

        // 画圆形
        circlePaint = new Paint();
        circlePaint.setColor(0xff868686);
//        circlePaint.setStyle(Paint.Style.FILL);// 充满
        circlePaint.setStyle(Paint.Style.STROKE);// 镶边

        // 画圆角矩形
        mRoundRectFPaint = new Paint();
        mRoundRectFPaint.setColor(0xff868686);

        // 圆角矩形的画笔
        mOvalPaint = new Paint();
        mOvalPaint.setColor(0xff868686);

        // 弧
        mArcPaint = new Paint();
        mArcPaint.setColor(0xff868686);

        // 多边形
        mMoreFigurePaint = new Paint();
        mMoreFigurePaint.setColor(0xff868686);
        mMoreFigurePaint.setStyle(Paint.Style.STROKE);// 镶边

        // 文本
        textPaint = new Paint();
        textPaint.setColor(0xff868686);
        textPaint.setTextSize(Utils.sp2px(mContext, 13));

        // 位图
        mBitmapPaint = new Paint();

        // 裁剪画布里的矩形
        cropPaint = new Paint();
        cropRectF = new RectF(0, 0
                , Utils.dip2px(mContext, 75f)// 单位都是px
                , Utils.dip2px(mContext, 15f));
    }

    /**
     *  画线的方法
     */
    private void drawLine(Canvas canvas) {
        /**
         * @params startX 线段起点的x坐标
         * @params startY 线段起点的Y坐标
         * @params stopX 线段终点的x坐标
         * @params stopY 线段终点y的坐标
         */
        canvas.drawLine(0,0
                , Utils.dip2px(mContext, 75f)
                , Utils.dip2px(mContext, 37.5f)
                , linePaint);
    }

    /**
     *  画矩形的方法
     */
    private void drawRect(Canvas canvas) {
        /**
         * RectF:
         *  left 矩形左侧的x坐标
         *  top 矩形顶部的y坐标
         *  right 矩形右侧的x坐标
         *  bottom 矩形底部的y坐标
         */
        if (rectF == null)
            rectF = new RectF(0, 0
                    , Utils.dip2px(mContext, 75)// 单位都是px
                    , Utils.dip2px(mContext, 75));
        canvas.drawRect(rectF, rectPaint);
    }

    /**
     *  画圆的方法
     */
    private void drawCircle(Canvas canvas) {
        /**
         *  float cx 中心点的x坐标
         *  float cy 中心点的y坐标
         *  float radius 半径
         */
        canvas.drawCircle(Utils.dip2px(mContext, 37.5f)
                , Utils.dip2px(mContext, 37.5f)
                , Utils.dip2px(mContext, 37.5f)
                , circlePaint);
    }

    /**
     *  圆角矩形
     */
    private void drawRoundRect(Canvas canvas) {
        /**
         * RectF：矩形区域
         * rx：在x轴的半径，焦点在x轴的椭圆长半轴
         * ry：在y轴的半径，焦点在x轴的椭圆短半轴
         *  可以理解成，在rectF矩形左上角的一个长轴短轴分别为2rx、2ry的标准内切椭圆
         */
        if (mRoundRectF == null)
            mRoundRectF = new RectF(0, 0
                    , Utils.dip2px(mContext, 150f)// 单位都是px
                    , Utils.dip2px(mContext, 75f));
        canvas.drawRoundRect(mRoundRectF
                , Utils.dip2px(mContext, 36.5f)
                , Utils.dip2px(mContext, 18.25f)
                , mRoundRectFPaint);
    }

    /**
     * 画椭圆
     */
    private void drawOval(Canvas canvas) {
        if (mOvalRectF == null)
            mOvalRectF = new RectF(0, 0
                    , Utils.dip2px(mContext, 112.5f)// 单位都是px
                    , Utils.dip2px(mContext, 75f));
        canvas.drawOval(mOvalRectF, mOvalPaint);
    }

    /**
     * 画弧
     */
    private void drawArc(Canvas canvas) {
        /**
         * RectF：矩形边界
         * startAngle：开始弧的角度，手表3点钟的方向为0
         * sweepAngle：顺时针的扫描角度
         * useCenter：椭圆的中心是否包含在弧里
         */
        if (mArcRectF == null)
            mArcRectF = new RectF(0, 0
                    , Utils.dip2px(mContext, 112.5f)// 单位都是px
                    , Utils.dip2px(mContext, 112.5f));
        canvas.drawArc(mArcRectF
                , -150
                , 120
                , true
                , mArcPaint);
    }

    /**
     *  绘制多边形，这里以三角形为例
     */
    private void drawMoreFigure(Canvas canvas) {
        // 三角形的起点
        if(mMoreFIgurePath == null)
            mMoreFIgurePath = new Path();
        // 三角形的起点
        mMoreFIgurePath.moveTo(Utils.dip2px(mContext, 75), 0);
        // (75,0)->(0,75)画线
        mMoreFIgurePath.lineTo(0, Utils.dip2px(mContext, 75));
        // (0,75)->(150,75)画线
        mMoreFIgurePath.lineTo(Utils.dip2px(mContext, 150)
                , Utils.dip2px(mContext, 75));
        // (150,75)->(75,0)画线，常用close替代
//        mMoreFIgurePath.lineTo(Utils.dip2px(mContext, 75), 0);
        // 闭合路径
        mMoreFIgurePath.close();
        canvas.drawPath(mMoreFIgurePath, mMoreFigurePaint);
    }

    /**
     * 填充颜色
     */
    private void drawCorlor(Canvas canvas) {
        canvas.drawColor(0xFFCCFFFF, PorterDuff.Mode.SRC_OVER);
    }

    /**
     * 绘制文本
     */
    private void drawText(Canvas canvas) {
        /**
         *  text：绘制文本
         *  textX：绘制文本的原点x坐标
         *  textY：绘制文本基线的y坐标
         */
        canvas.drawText("我和我的祖国"
                , Utils.dip2px(mContext, 0f)
                , Utils.dip2px(mContext, 75f)
                , textPaint);
    }

    /**
     * 绘制图片
     */
    private void drawBitmap(Canvas canvas) {
        /**
         * bitmap
         *  left：绘制的位图的左侧位置
         *  top：绘制位图的上方位置
         */
        if (mBitmap == null) {
            // 将资源图片转换成bitmap,R.mipmap.android:资源图片
            mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_android);
            // 将mBitmap缩放成固定大小
            mBitmap = Utils.conversionBitmap(mBitmap
                    , Utils.dip2px(mContext, 36)
                    , Utils.dip2px(mContext, 42));
        }
        canvas.drawBitmap(mBitmap
                , 0
                , Utils.dip2px(mContext, 54)
                , mBitmapPaint);
    }

    /**
     * 画布的裁剪
     */
    private void drawClipPathOnCanval(Canvas canvas) {
        if (mClipPath == null) {
            mClipPath = new Path();
            // path为圆形矩形。裁剪圆形，弧等都同理
            if (mClipRectF == null)
                mClipRectF = new RectF(0, 0
                        , Utils.dip2px(mContext, 75f)// 单位都是px
                        , Utils.dip2px(mContext, 150f));
            /**
             * RectF：矩形轮廓
             * rx：圆角矩形的圆角的x半径
             * ry：圆角矩形的圆角的y半径
             * direction：cw:顺时针、CCW:逆时针
             */
            mClipPath.addRoundRect(mClipRectF
                    , Utils.dip2px(mContext, 7.5f)
                    , Utils.dip2px(mContext, 7.5f)
                    , Path.Direction.CW);
        }
        canvas.clipPath(mClipPath);
    }

    /**
     * 画布的旋转
     */
    private void drawRotate(Canvas canvas) {
        // 画10条线，画线的方法同上
        for (int index = 0; index < 5; index ++) {
            // 画布旋转的角度,每次+10
            canvas.rotate(10f);
            drawLine(canvas);
        }
    }

    /**
     * 画布的平移
     */
    private void drawTranslate(Canvas canvas){
        /**
         * dx: 要在x中转换的距离
         * dy: 要在y中转换的距离
         */
        canvas.translate(Utils.dip2px(mContext, 75)
                , Utils.dip2px(mContext, 75));
    }

    private void drawRectInCropCanvas(Canvas canvas) {
        if (cropPaint == null || cropRectF == null)
            return;
        // 在裁剪的画布里画一个矩形
        cropPaint.setColor(0xffd96b63);
        cropRectF.top = 0;
        cropRectF.bottom = Utils.dip2px(mContext, 15f);
        canvas.drawRect(cropRectF, cropPaint);

        // 再画一个矩形
        cropPaint.setColor(0xff6cbe89);
        cropRectF.top = Utils.dip2px(mContext, 15f);
        cropRectF.bottom = Utils.dip2px(mContext, 75f);
        canvas.drawRect(cropRectF, cropPaint);
    }
}
