package com.mjzuo.views.figure;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.mjzuo.views.BaseActivity;
import com.mjzuo.views.R;
import com.ui.common.view.GeometricFigureView;

/**
 * 基础绘制接口类示例。
 *
 * @author mjzuo
 * @Data 19/11/19
 */
public class FigureViewActivity extends BaseActivity implements View.OnClickListener {

    GeometricFigureView figureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.figure_view_layout);

        figureView = findViewById(R.id.view_figure);
        registerClick();
    }

    private void registerClick() {
        findViewById(R.id.view_line).setOnClickListener(this);
        findViewById(R.id.view_rect).setOnClickListener(this);
        findViewById(R.id.view_circle).setOnClickListener(this);
        findViewById(R.id.view_oval).setOnClickListener(this);
        findViewById(R.id.view_round_rect).setOnClickListener(this);
        findViewById(R.id.view_arc).setOnClickListener(this);
        findViewById(R.id.view_more_figure).setOnClickListener(this);
        findViewById(R.id.view_color).setOnClickListener(this);
        findViewById(R.id.view_text).setOnClickListener(this);
        findViewById(R.id.view_bitmap).setOnClickListener(this);
        findViewById(R.id.view_crop).setOnClickListener(this);
        findViewById(R.id.view_rotate).setOnClickListener(this);
        findViewById(R.id.view_translate).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_line:
                changeViewType(GeometricFigureView.LINE_TYPE);
                break;

            case R.id.view_rect:
                changeViewType(GeometricFigureView.RECT_TYPE);
                break;

            case R.id.view_circle:
                changeViewType(GeometricFigureView.CIRCLE_TYPE);
                break;

            case R.id.view_oval:
                changeViewType(GeometricFigureView.OVAL_TYPE);
                break;

            case R.id.view_round_rect:
                changeViewType(GeometricFigureView.ROUND_CIRCLE_TYPE);
                break;

            case R.id.view_arc:
                changeViewType(GeometricFigureView.ARC_TYPE);
                break;

            case R.id.view_more_figure:
                changeViewType(GeometricFigureView.MORE_FIGURE_TYPE);
                break;

            case R.id.view_color:
                changeViewType(GeometricFigureView.FILL_CORLOR);
                break;

            case R.id.view_text:
                changeViewType(GeometricFigureView.TEXT_CORLOR);
                break;

            case R.id.view_bitmap:
                changeViewType(GeometricFigureView.BITMAP_TYPE);
                break;

            case R.id.view_crop:
                changeViewType(GeometricFigureView.CLIP_TYPE);
                break;

            case R.id.view_rotate:
                changeViewType(GeometricFigureView.STATE_TYPE);
                break;

            case R.id.view_translate:
                changeViewType(GeometricFigureView.TRANSLATE_TYPE);
                break;
        }
    }

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
    private void changeViewType(int type) {
        figureView.setDrawType(type);
    }
}
