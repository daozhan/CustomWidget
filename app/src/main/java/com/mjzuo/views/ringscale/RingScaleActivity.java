package com.mjzuo.views.ringscale;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mjzuo.views.BaseActivity;
import com.mjzuo.views.R;
import com.ui.common.view.RingScaleView;
import com.ui.common.view.SeekControllerView;
import com.ui.common.view.RingScaleSlideView;

/**
 * 这是刻度条用法示例。
 *
 * @author mjzuo
 * @Data 19/11/19
 */
public class RingScaleActivity extends BaseActivity {

    // 滑动条VIEW
    private SeekControllerView seekController;
    // 圆环刻度VIEW
    private RingScaleView ringScaleView;
    // 可拖动动环形刻度条
    private RingScaleSlideView ringScaleSlideView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring_scale_layout);

        seekController = findViewById(R.id.seek_controller);
        ringScaleView = findViewById(R.id.ring_scale_view);
        ringScaleSlideView = findViewById(R.id.slide_ring_scale_view);

        seekController.setOnScaleChangeListener(new SeekControllerView.OnScaleChangeListener() {
            @Override
            public void onScaleBy(int scaleBy) {
                // 刻度变化监听
                ringScaleView.setProgress(scaleBy);
                ringScaleSlideView.setProgress(scaleBy);
            }
        });
    }
}
