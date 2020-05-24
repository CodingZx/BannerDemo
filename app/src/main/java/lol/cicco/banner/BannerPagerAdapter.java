package lol.cicco.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class BannerPagerAdapter extends PagerAdapter {

    private final List<Integer> res;

    BannerPagerAdapter(List<Integer> res) {
        this.res = res;
    }

    @Override
    public int getCount() {
        return res.size();
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
        // 创建控件
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner, container, false);
        ImageView imageView = itemView.findViewById(R.id.cover);
        imageView.setImageResource(res.get(position));
        if(imageView.getParent() instanceof ViewGroup) {
            ((ViewGroup) imageView.getParent()).removeView(imageView);
        }
        container.addView(imageView);
        return imageView;
    }

    /**
     * 销毁....
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
