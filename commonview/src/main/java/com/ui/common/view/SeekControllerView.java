package com.ui.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ui.common.R;

/**
 * 滑动条VIEW。
 *
 * <p>被用做地图APP控制地图缩放级别。
 *
 * @author mjzuo
 * @since 19/12/25
 */
public class SeekControllerView extends RelativeLayout {

    private ImageView seekButton;
    private View lineView;

    private OnScaleChangeListener onScaleChangeListener;

    private int middleScale = 50;

    private Context mContext;

    public SeekControllerView(Context context) {
        this(context, null);
    }

    public SeekControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        inflate(mContext, R.layout.seek_layout, this);

        lineView = findViewById(R.id.seek_controller_line);
        seekButton = findViewById(R.id.seek_img);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int h = getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (y < h / 2 - (seekButton.getHeight() / 2)
                        || y > h / 2 + (seekButton.getHeight() / 2)) {
                    return false;
                }
                lineView.setVisibility(VISIBLE);
                break;

            case MotionEvent.ACTION_MOVE:
                float scale = 0;
                if(y <= h / 2) {
                    float distance = h / 2 - y;
                    scale = middleScale - middleScale * 1.0f * distance / (h / 2);
                    if(scale < 0)
                        scale = 0;
                }
                if(y > h / 2) {
                    float distance = y - h / 2;
                    scale = middleScale + middleScale * 1.0f / (h / 2) * distance;
                    if(scale > 2 * middleScale)
                        scale = 2 * middleScale;
                }
                if (onScaleChangeListener != null) {
                    onScaleChangeListener.onScaleBy((int)scale);
                }
                seekButton.layout(0, y - seekButton.getWidth() / 2
                        , seekButton.getWidth()
                        , y - seekButton.getWidth() / 2 + seekButton.getHeight());
                break;

            case MotionEvent.ACTION_UP:
                lineView.setVisibility(GONE);
                if (onScaleChangeListener != null && middleScale != 0) {
                    onScaleChangeListener.onScaleBy(middleScale);
                }
                break;

        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.onScaleChangeListener = onScaleChangeListener;
    }

    public interface OnScaleChangeListener {

        void onScaleBy(int scaleBy);

    }
}
