package lol.cicco.banner.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import lol.cicco.banner.R;
import lol.cicco.banner.SizeUtils;

public class CiccoLooperView extends LinearLayout {
    private static final String TAG = "CiccoLooperView";

    private CiccoViewPager pager;
    private TextView textView;
    private LinearLayout layout;
    private List<LooperViewData> dataList = new ArrayList<>();
    private List<View> bottomViews = new ArrayList<>();

    public interface LooperViewData {
        String getTitle();
        int getResource();
    }

    public CiccoLooperView(Context context) {
        this(context, null);
    }

    public CiccoLooperView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CiccoLooperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.looper_page, this, true);
        // 等价于下面
//        View looperPager = LayoutInflater.from(context).inflate(R.layout.looper_page, this, false);
//        addView(looperPager);

        pager = findViewById(R.id.ciccoViewPager);
        textView = findViewById(R.id.ciccoTextView);
        layout = findViewById(R.id.ciccoLayout);
        initListener();
    }

    private void initListener() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int nowPosition = dataList.size() == 1 ? 0 : 1;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 切换
            }

            @Override
            public void onPageSelected(int position) {
                // 切换停止
                changeState(position);
                nowPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 状态的改变
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (nowPosition == 0) {
                        pager.setCurrentItem(dataList.size(), false);
                    }
                    if (nowPosition == dataList.size() + 1) {
                        pager.setCurrentItem(1, false);
                    }
                }
            }
        });
    }

    public void startLooper(long delayMillis) {
        pager.startLooper(delayMillis);
    }

    public void stopLooper() {
        pager.stopLooper();
    }

    public void setData(final List<LooperViewData> looperViewData) {
        dataList.clear();
        dataList.addAll(looperViewData);

        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                if(dataList.size() == 1) {
                    return 1;
                }
                return dataList.size() + 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
            /**
             * 初始化..
             */
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                Log.d(TAG, "instantiateItem..." + position);
                // 创建控件
                ImageView imageView = new ImageView(container.getContext());
                imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner, container, false);
//                ImageView imageView = itemView.findViewById(R.id.cover);
                imageView.setImageResource(looperViewData.get(getRealIndex(position)).getResource());
                if (!(imageView.getParent() instanceof ViewGroup)) {
                    container.addView(imageView);
                }
                return imageView;
            }

            /**
             * 销毁....
             */
            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
                Log.d(TAG, "destroyItem..." + position);
            }
        };
        pager.setAdapter(adapter);
        pager.setClickListener(new CiccoViewPager.ItemClickListener() {
            @Override
            public void click(int position) {
                int realIndex = getRealIndex(position);
                Toast.makeText(getContext(), "点击了第"+(realIndex + 1)+"张图片", Toast.LENGTH_LONG).show();
            }
        });

        layout.removeAllViews();
        bottomViews.clear();
        for (int i = 0; i < looperViewData.size(); i++) {
            View v = new View(this.getContext());
            LayoutParams layoutParams = new LayoutParams(SizeUtils.dip2px(getContext(), 5), SizeUtils.dip2px(getContext(), 5));
            layoutParams.setMarginStart(SizeUtils.dip2px(getContext(), 5));
            v.setLayoutParams(layoutParams);
            v.setBackgroundResource(R.drawable.shape_white);
            bottomViews.add(v);
            layout.addView(v);
        }
        changeState(0);

        // 设置为第一个
        if(looperViewData.size() > 1) {
            pager.setCurrentItem(1);
        }
    }

    private void changeState(int position){
        int realIndex = getRealIndex(position);
        // 切换停止
        textView.setText(dataList.get(realIndex).getTitle());

        for (int i = 0; i < bottomViews.size(); i++) {
            if(realIndex == i) {
                bottomViews.get(realIndex).setBackgroundResource(R.drawable.shape_red);
            } else {
                bottomViews.get(i).setBackgroundResource(R.drawable.shape_white);
            }
        }
    }

    // 无限滑动 计算index
    private int getRealIndex(int position) {
        if(dataList.size() == 1) {
            return 0;
        }
        if(position == 0) {
            return dataList.size() - 1;
        }
        if(position == dataList.size() + 1) {
            return 0;
        }
        return position - 1;
    }

}
