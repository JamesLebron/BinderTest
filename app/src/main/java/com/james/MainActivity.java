package com.james;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 在service类中，创建一个满足以下任一要求的Binder实例：
 * 包含客户端可调用的公共方法
 * 返回当前Service实例，其中包含客户端可调用的公共方法
 * 返回由当前service承载的其他类的实例，其中包含客户端可调用的公共方法
 * 在onBind()方法中返回这个Binder实例
 * 在客户端中通过onServiceDisconnected()方法接收传过去的Binder实例，并通过它提供的方法进行后续操作
 * 可以看到，在使用这种方法进行客户端与服务端之间的交互是需要有一个强制类型转换的——在onServiceDisconnected()
 * 中获得一个经过转换的IBinder对象，我们必须将其转换为service类中的Binder实例的类型才能正确的调用其方法。
 * 而这强制类型转换其实就隐含了一个使用这种方法的条件：客户端和服务端应当在同一个进程中！不然在类型转换的时候也许会出现问题
 */
public class MainActivity extends AppCompatActivity {
    private boolean isBind;
    private LocalService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.bt_do);
        Button button1 = (Button) findViewById(R.id.bt1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBind) {
                    int randomNumber = mService.getRandomNumber();
                    Toast.makeText(MainActivity.this, "从本进程的service获取的随机数" + randomNumber,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MessengerActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        if (isBind) {
            unbindService(mConnection);
            isBind = false;
        }
        super.onStop();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };
}
