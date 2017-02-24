package com.james;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class LocalService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private final Random mRandom = new Random();

    public LocalService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    public int getRandomNumber() {
        return mRandom.nextInt(1000);
    }

}
