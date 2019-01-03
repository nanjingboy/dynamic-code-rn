package com.dynamiccodern;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

public class DynamicActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    private ReactRootView mReactRootView;
    private DynamicReactActivityDelegate mDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        findViewById(R.id.goBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDelegate = new DynamicReactActivityDelegate(this, getIntent().getStringExtra("filePath"));
        mReactRootView = findViewById(R.id.reactRootView);
        mReactRootView.startReactApplication(mDelegate.getReactInstanceManager(), "DynamicCodeRN");
    }


    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelegate.onDestroy();
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
    }

    @Override
    public void onBackPressed() {
        mDelegate.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mDelegate.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }
}
