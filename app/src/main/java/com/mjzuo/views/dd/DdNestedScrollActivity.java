package com.mjzuo.views.dd;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mjzuo.common.widget.DDViewGroup;
import com.mjzuo.views.R;
import com.tencent.tencentmap.mapsdk.maps.MapView;

import java.util.ArrayList;

/**
 * 仿滴滴首页嵌套滑动示例。
 *
 * @author mjzuo
 * @Data 20/06/13
 */
public class DdNestedScrollActivity extends AppCompatActivity {

    static final String LOG_TAG = ">>dd";

    // 腾讯地图MAP
    private MapView mapView;

    private RecyclerView recyclerView;
    private DDViewGroup ddViewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestNoTitle(this);
        setContentView(R.layout.dd_nested_scroll_layout);

        mapView = findViewById(R.id.t_map_view);
        recyclerView = findViewById(R.id.inner_rv);
        ddViewGroup = findViewById(R.id.dd_view_group);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new DdNestedScrollActivity.MyRvAdapter());

        ddViewGroup.setOnScrollListener(new DDViewGroup.IScrollListener() {
            @Override
            public void onTargetToTopDistance(int distance) {
                Log.e(LOG_TAG, "target top :" + distance);
            }

            @Override
            public void onHeaderToTopDistance(int distance) {
                Log.e(LOG_TAG, "header top :" + distance);
            }
        });

    }

    protected void requestNoTitle(Activity activity) {
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity)activity).getSupportActionBar().hide();
        } else {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mapView)
            mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mapView)
            mapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mapView)
            mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mapView)
            mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mapView)
            mapView.onDestroy();
    }

    class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.ViewHolder> {

        ArrayList<String> cvData = new ArrayList<>();

        public MyRvAdapter() {

            if(cvData.size() != 0)
                cvData.clear();
            for (int i = 0; i < 20; i++) {
                String content = "第" + i + "项";
                cvData.add(content);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.common_rv_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String content = cvData.get(position);
            holder.tvContent.setText(content);
        }

        @Override
        public int getItemCount() {
            return cvData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout allLayout;
            TextView tvContent;

            public ViewHolder(View view) {
                super(view);
                tvContent = view.findViewById(R.id.main_rv_item_content);
                allLayout = view.findViewById(R.id.all_layout);
            }
        }
    }

}
