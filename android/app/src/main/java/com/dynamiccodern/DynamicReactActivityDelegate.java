package com.dynamiccodern;

import android.app.Activity;
import android.view.KeyEvent;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.DoubleTapReloadRecognizer;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;

public class DynamicReactActivityDelegate {

    private Activity mActivity;
    private ReactInstanceManager mReactInstanceManager;
    private DoubleTapReloadRecognizer mDoubleTapReloadRecognizer;

    public DynamicReactActivityDelegate(Activity activity, String jsFilePath) {
        mActivity = activity;
        mDoubleTapReloadRecognizer = new DoubleTapReloadRecognizer();
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(activity.getApplication())
                .setJSBundleFile(jsFilePath)
                .setJSMainModulePath("index")
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .addPackage(new MainReactPackage())
                .build();
    }

    public ReactInstanceManager getReactInstanceManager() {
        return mReactInstanceManager;
    }


    public void onPause() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(mActivity);
        }
    }

    public void onResume() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(mActivity, (DefaultHardwareBackBtnHandler) mActivity);
        }
    }

    public void onDestroy() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(mActivity);
        }
    }

    public void onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (BuildConfig.DEBUG && mReactInstanceManager != null) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                mReactInstanceManager.showDevOptionsDialog();
                return true;
            }
            if (mDoubleTapReloadRecognizer != null &&
                    mDoubleTapReloadRecognizer.didDoubleTapR(keyCode, mActivity.getCurrentFocus())) {
                mReactInstanceManager.getDevSupportManager().handleReloadJS();
                return true;
            }
        }
        return false;
    }
}
