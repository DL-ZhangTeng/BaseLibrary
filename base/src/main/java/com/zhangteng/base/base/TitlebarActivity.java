package com.zhangteng.base.base;

import android.view.View;

import com.zhangteng.base.R;
import com.zhangteng.base.widget.CommonTitleBar;


/**
 * Created by swing on 2017/11/23.
 */

public abstract class TitlebarActivity extends BaseActivity {
    protected CommonTitleBar mTitlebar;

    @Override
    protected void initView() {
        mTitlebar = findViewById(R.id.titlebar);
        if (mTitlebar == null) {
            throw new IllegalStateException(
                    "The subclass of TitlebarActivity must contain a titlebar.");
        }
        mTitlebar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON || action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                }
            }
        });
    }
}

