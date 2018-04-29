package george.ni.medicare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import george.ni.medicare.R;

/**
 * Created by Thinkpad on 2018/4/19.
 */

public class StartActivity extends BaseActivity {
    private Handler mHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initHandler();
        //延迟进入主页面
        goToMain();
    }

    private void goToMain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity,MainActivity.class);
                startActivity(intent);
                StartActivity.this.finish();
            }
        },1000);
    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
    }
}
