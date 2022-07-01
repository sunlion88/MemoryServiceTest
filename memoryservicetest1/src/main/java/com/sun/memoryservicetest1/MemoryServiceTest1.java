package com.sun.memoryservicetest1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MemoryServiceTest1 extends Service {
    private static final String TAG ="MemoryServiceTest1";

    private NotificationManager notificationManager;
    private static final String NOTIFICATION_ID = "channelId";
    private static final String NOTIFICATION_NAME ="channelName";

    List<Object> memoryList = new ArrayList<Object>();

    public MemoryServiceTest1() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID,NOTIFICATION_NAME,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        startForeground(1,getNotification());
        new Thread(new Runnable() {
            @Override
            public void run() {
//                while (true)
//                {
                    Request500MMemory();
//                }

            }
        }).start();
    }
    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        free500MMemory();
        super.onDestroy();
    }
    private Notification getNotification()
    {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("服务测试")
                .setContentText("MemoryServiceTest正在运行");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
                builder.setChannelId(NOTIFICATION_ID);
        }
        Notification notification = builder.build();
        return notification;
    }

    private void Request500MMemory() {
        for (int i = 0; i < 188; i++) {
            memoryList.add(new MemoryTest1(new char[1024*1024*1]));
        }

//        char[] memoryArr = new char[1024*1024*62];
        float totalMemory = (float) (Runtime.getRuntime().totalMemory()*1.0/(1024*1024));
        Log.d(TAG,"memoryList size ==" + memoryList.size() + " TotalMemory size: "+ totalMemory +"MB");

    }
    private void free500MMemory() {
        memoryList.clear();
    }
}