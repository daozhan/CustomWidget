package com.mjzuo.views.dd;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.mjzuo.views.BaseActivity;
import com.mjzuo.views.R;
import com.ui.common.view.DdSpotMarkerView;
import com.ui.common.view.DdSpotView;

/**
 * 仿滴滴Ui大头针跳动和上车点效果示例。
 *
 * @author mjzuo
 * @Data 19/12/17
 */
public class DdSpotActivity extends BaseActivity {

    private DdSpotView tvStrokeTxtLeft, tvStrokeTxtRight;
    private DdSpotMarkerView ddSpotMarkerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_spot_and_marker_layout);

        tvStrokeTxtLeft = findViewById(R.id.spot_left_view);
        tvStrokeTxtRight = findViewById(R.id.spot_right_view);
        ddSpotMarkerView = findViewById(R.id.spot_marker_view);

        tvStrokeTxtLeft.setTitle("从前有座山山里有个庙");
        tvStrokeTxtLeft.setDirectionType(1);
        tvStrokeTxtLeft.statrInvalidata();
        tvStrokeTxtRight.setTitle("庙里有小和尚");
        tvStrokeTxtRight.setDirectionType(0);
        tvStrokeTxtRight.statrInvalidata();

        findViewById(R.id.start_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddSpotMarkerView.startLoadingAnima();
            }
        });

        findViewById(R.id.stop_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddSpotMarkerView.stopLoadingAnima();
                ddSpotMarkerView.transactionAnimWithMarker().addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ddSpotMarkerView.startRippleAnima();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });

    }
}
