package com.mjzuo.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mjzuo.views.dd.DdNestedScrollActivity;
import com.mjzuo.views.dd.DdSpotActivity;
import com.mjzuo.views.figure.FigureViewActivity;
import com.mjzuo.views.music.MusicActivity;
import com.mjzuo.views.ringscale.RingScaleActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 首页展示界面。
 */
public class MainActivity extends BaseActivity {

    // 基础绘制接口类示例
    public static final int NORMAL_VIEW = 0;
    // 刻度条用法示例
    public static final int RING_SCALE = 1;
    // 仿滴滴上车点和大头针动画示例
    public static final int DD_SPOT = 2;
    // 仿网易云音乐鲸云音效示例
    public static final int WY_MUSIC = 3;
    // 仿滴滴首页嵌套滑动示例
    public static final int DD_NESTED_SCROLL = 4;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        recyclerView = findViewById(R.id.main_rv);
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(new RvAdapter(new IClickListener() {
            @Override
            public void onClick(int itemPosition) {
                clickItem(itemPosition);
            }
        }));
    }

    private void clickItem(int itemPosition) {
        switch (itemPosition){
            case NORMAL_VIEW:
                // 基础绘制接口类示例
                toIntent(FigureViewActivity.class);
                break;

            case RING_SCALE:
                // 刻度条用法示例
                toIntent(RingScaleActivity.class);
                break;

            case DD_SPOT:
                // 仿滴滴上车点和大头针动画示例
                toIntent(DdSpotActivity.class);
                break;

            case DD_NESTED_SCROLL:
                // 仿滴滴首页嵌套滑动示例
                toIntent(DdNestedScrollActivity.class);
                break;

            case WY_MUSIC:
                // 仿网易云音乐鲸云音效示例
                toIntent(MusicActivity.class);
                break;

        }
    }

    private void toIntent(Class<? extends AppCompatActivity> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

        ArrayList<String> cvData = new ArrayList<>();
        IClickListener clickListener;

        public RvAdapter(IClickListener listener) {
            this.clickListener = listener;
            if(cvData.size() != 0)
                cvData.clear();
            addData();
        }

        private void addData() {
            cvData.add("基础绘制接口类示例");
            cvData.add("刻度条用法");
            cvData.add("仿滴滴上车点和大头针动画");
            cvData.add("仿网易云音乐鲸云音效");
            cvData.add("仿滴滴首页嵌套滑动");
        }

        @NonNull
        @Override
        public RvAdapter.ViewHolder onCreateViewHolder
                (@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_rv_item_layout
                            , parent, false);
            return new RvAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder
                (@NonNull RvAdapter.ViewHolder holder, int position) {
            String content = cvData.get(position);
            holder.tvContent.setText(content);

            holder.listener.position = position;
            holder.listener.setViewHolder(holder);
            holder.allLayout.setOnClickListener(holder.listener);
        }

        @Override
        public int getItemCount() {
            return cvData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            RvAdapter.MyClickListener listener = new RvAdapter.MyClickListener();

            LinearLayout allLayout;
            TextView tvContent;

            public ViewHolder(View view) {
                super(view);
                tvContent = view.findViewById(R.id.main_rv_item_content);
                allLayout = view.findViewById(R.id.all_layout);
            }
        }

        class MyClickListener implements View.OnClickListener {
            public WeakReference<RvAdapter.ViewHolder> wrf;
            public int position;

            public void setViewHolder(RvAdapter.ViewHolder viewHolder) {
                wrf = new WeakReference<>(viewHolder);
            }

            @Override
            public void onClick(View v) {
                if (wrf == null || wrf.get() == null) {
                    return;
                }
                if (clickListener != null) {
                    clickListener.onClick(position);
                }
            }
        }

    }

    /**
     * 列表的点击监听。
     */
    interface IClickListener {
        void onClick(int itemPosition);
    }

}
