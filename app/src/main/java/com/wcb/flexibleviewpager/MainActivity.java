package com.wcb.flexibleviewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wcb.flexibleviewpager.adapter.MyViewPagerAdapter;
import com.wcb.flexibleviewpager.view.FlexibleViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FlexibleViewPager.OnRefreshListener {
    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;
    private List<View> list = new ArrayList<>();
    private int[] imgs = {R.mipmap.pic1, R.mipmap.pic2, R.mipmap.pic3};
    private FlexibleViewPager mFlexibleViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mFlexibleViewPager = (FlexibleViewPager) findViewById(R.id.fvp);
        initData();
        adapter = new MyViewPagerAdapter(list);
        viewPager.setAdapter(adapter);
        mFlexibleViewPager.setOnRefreshListener(this);
    }

    private void initData() {
        for (int img : imgs) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(img);
            list.add(imageView);
        }
    }

    @Override
    public void onRefresh() {
        showToast("刷新");
    }

    @Override
    public void onLoadMore() {
        showToast("加载更多");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
