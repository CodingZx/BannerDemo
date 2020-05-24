package lol.cicco.banner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import lol.cicco.banner.views.CiccoLooperView;

public class SuperActivity extends AppCompatActivity {

    private CiccoLooperView looperView;
    private static final List<CiccoLooperView.LooperViewData> bannerResource = new ArrayList<>(3);

    static {
        bannerResource.add(new CiccoLooperView.LooperViewData() {
            @Override
            public String getTitle() {
                return "我是第一张图片";
            }

            @Override
            public int getResource() {
                return R.mipmap.banner_1;
            }
        });
        bannerResource.add(new CiccoLooperView.LooperViewData() {
            @Override
            public String getTitle() {
                return "我是第二张图片";
            }

            @Override
            public int getResource() {
                return R.mipmap.banner_2;
            }
        });
        bannerResource.add(new CiccoLooperView.LooperViewData() {
            @Override
            public String getTitle() {
                return "我是第三张图片";
            }

            @Override
            public int getResource() {
                return R.mipmap.banner_3;
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(looperView != null) {
            looperView.stopLooper();
        }
    }

    private void initView() {
        looperView = findViewById(R.id.superLooperView);
        looperView.setData(bannerResource);
        looperView.startLooper(2000);
    }
}
