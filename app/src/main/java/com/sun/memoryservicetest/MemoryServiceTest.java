package com.sun.memoryservicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MemoryServiceTest extends Service {
    private static final String TAG = "MemoryServiceTest";

    private NotificationManager notificationManager;
    private static final String NOTIFICATION_ID = "channelId";
    private static final String NOTIFICATION_NAME = "channelName";

    List<Object> memoryList = new ArrayList<Object>();

    public MemoryServiceTest() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        startForeground(1, getNotification());
        new Thread(new Runnable() {
            @Override
            public void run() {
//                while (true)
//                {
                Request500MMemory();
//                }

            }
        }).start();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.sun.memoryservicetest1", "com.sun.memoryservicetest1.MemoryServiceTest1"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        free500MMemory();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.sun.memoryservicetest1", "com.sun.memoryservicetest1.MemoryServiceTest1"));
        stopService(intent);
        super.onDestroy();
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("服务测试")
                .setContentText("MemoryServiceTest正在运行");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_ID);
        }
        Notification notification = builder.build();
        return notification;
    }
    //    #通过 getprop | grep heap查看
//    RT2851:/ # getprop | grep heap
//[dalvik.vm.heapgrowthlimit]: [128m]
//            [dalvik.vm.heapmaxfree]: [8m]
//            [dalvik.vm.heapminfree]: [512k]
//            [dalvik.vm.heapsize]: [384m]
//            [dalvik.vm.heapstartsize]: [8m]
//            [dalvik.vm.heaptargetutilization]: [0.75]
//            [ro.af.client_heap_size_kbyte]: [4096]
//    heapgrowthlimit表示单个app可以申请的最大内存空间
//    heapsize 表示 在AndroidManifest.xml下设置 android:lagerHeap=true时单个app可以申请的最大内存空间
    private void Request500MMemory() {
        for (int i = 0; i < 188; i++) {
            memoryList.add(new MemoryTest(new char[1024 * 1024 * 1]));
        }
//        char[] memoryArr = new char[1024*1024*62];
        float totalMemory = (float) (Runtime.getRuntime().totalMemory() * 1.0 / (1024 * 1024));
        Log.d(TAG, "memoryList size ==" + memoryList.size() + " TotalMemory size: " + totalMemory + "MB");

    }

    private void free500MMemory() {
        memoryList.clear();
    }
}