package com.james;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

/**
 * 客户端使用 IBinder 将 Messenger（引用服务的 Handler）实例化，然后使用后者将 Message 对象发送给服务
 * 服务端在其 Handler 中（具体地讲，是在 handleMessage() 方法中）接收每个 Message
 * 用这种方式，客户端并没有像扩展Binder类那样直接调用服务端的方法，而是采用了用Message来传递信息的方式达到交互的目的
 */
public class MessengerActivity extends AppCompatActivity {

    private boolean isBind;
    private android.os.Messenger mMessenger = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);
        Button button = (Button) findViewById(R.id.bt_do);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBind) {
                    envocallRomote();
                }
            }
        });
    }

    private void envocallRomote() {
        if (!isBind)
            return;
        Message msg = Message.obtain();
        msg.what = new Random().nextInt(100);
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        //添加远程Service的action 和包名
        Intent intent = new Intent("com.james.messenger.action");
        intent.setPackage("com.james.messenger");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new android.os.Messenger(service);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger = null;
            isBind = false;
        }
    };
}
