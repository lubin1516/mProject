package com.lide.app.ui.outbound;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lide.app.R;
import com.lide.app.adapter.ViewPagerAdapter;
import com.lide.app.persistence.view.NoScrollViewPager;
import com.lide.app.ui.BaseActivity;
import com.lide.app.ui.FragmentBase;
import com.lide.app.ui.outbound.createOrder.CreateOutBoundOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lubin on 2016/11/16.
 */

public class ControlOutBoundActivity extends BaseActivity {
    @BindView(R.id.tv_common)
    TextView tvCommon;
    @BindView(R.id.tb_common)
    Toolbar tbCommon;
    @BindView(R.id.vp_common)
    NoScrollViewPager vpCommon;

    public List<FragmentBase> mFragments;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(tbCommon);
        tbCommon.setNavigationIcon(R.mipmap.back_login);
        tbCommon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tbCommon.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        break;
                }
                return true;
            }
        });

        mFragments = new ArrayList<>();
        mFragments.add(new CreateOutBoundOrderFragment());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        vpCommon.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvCommon.setText("出库");
        tbCommon.setTitle("");
    }
}
