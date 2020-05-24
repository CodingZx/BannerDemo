package lol.cicco.banner.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CiccoViewPager extends ViewPager {
    private static final String TAG = "CiccoViewPager";
    private long delayMillis = 1000;

    private ItemClickListener itemClickListener;

    public CiccoViewPager(@NonNull Context context) {
        this(context, null);
    }

    public CiccoViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // 自动滚动
    private boolean looperStartState = false;
    private boolean userLooperStart = false;
    private Runnable looperRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            setCurrentItem(currentItem+1);
            postDelayed(this, delayMillis);
        }
    };

    public void startLooper(long delayMillis) {
        this.delayMillis = delayMillis;
        this.userLooperStart = true;
        start();
    }

    private void start() {
        if(!userLooperStart) {
            return;
        }
        if(!looperStartState) {
            Log.d(TAG, "startLooper... delayMillis -> "+ delayMillis);
            looperStartState = true;
            postDelayed(looperRunnable, delayMillis);
        }
    }

    public void stopLooper() {
        if(looperStartState) {
            Log.d(TAG, "stopLooper...");
            removeCallbacks(looperRunnable);
            looperStartState = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(userLooperStart) {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLooper();
    }

    private float clickX;
    private float clickY;
    private long clickStartTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean superReturn = super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.clickX = event.getX();
                this.clickY = event.getY();
                this.clickStartTime = System.currentTimeMillis();
                stopLooper();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_DOWN  stop");
                stopLooper();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP start");
                float dx = Math.abs(clickX - event.getX());
                float dy = Math.abs(clickY - event.getY());
                if(dx <= 5 && dy <= 5 && (System.currentTimeMillis() - clickStartTime) <= 500) {
                    // 触发click
                    if(itemClickListener != null) {
                        itemClickListener.click(getCurrentItem());
                    }
                }
                start();
                break;
        }
        return superReturn;
    }

    public interface ItemClickListener {
        void click(int position);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

}
