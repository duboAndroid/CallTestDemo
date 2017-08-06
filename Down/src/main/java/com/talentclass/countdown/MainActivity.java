package com.talentclass.countdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 倒计时demo
 *
 * @author talentClass
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private Context mContext;
    private Intent mIntent;
    private Button btnCountdown;
    // 广播接收者
    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case RegisterCodeTimerService.IN_RUNNING:
                    if (btnCountdown.isEnabled())
                        btnCountdown.setEnabled(false);
                    // 正在倒计时
                    btnCountdown.setText("倒计时中(" + intent.getStringExtra("time") + ")");
                    Log.e(TAG, "倒计时中(" + intent.getStringExtra("time") + ")");
                    break;
                case RegisterCodeTimerService.END_RUNNING:
                    // 完成倒计时
                    btnCountdown.setEnabled(true);
                    btnCountdown.setText(R.string.countdown);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Log.e(TAG, "onStart 方法调用");
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册广播
        registerReceiver(mUpdateReceiver, updateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 移除注册
        unregisterReceiver(mUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy 方法调用");
    }

    // 注册广播
    private static IntentFilter updateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RegisterCodeTimerService.IN_RUNNING);
        intentFilter.addAction(RegisterCodeTimerService.END_RUNNING);
        return intentFilter;
    }

    // 初始化界面
    private void init() {
        mIntent = new Intent(mContext, RegisterCodeTimerService.class);
        btnCountdown = (Button) findViewById(R.id.activity_main_btn_countdown);
        btnCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将按钮设置为不可用状态
                btnCountdown.setEnabled(false);
                // 启动倒计时的服务
                startService(mIntent);
            }
        });

    }
}
