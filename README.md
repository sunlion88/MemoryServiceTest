#因为有两台机器，一个机器DDR是1.5G内存的，另一台机器DDR内存是2G的
#现在需要对比测试性能，为了排查DDR的内存大小的干扰，为此开了需要开一个service让它占用500M的内存
#通过 getprop | grep heap查看
RT2851:/ # getprop | grep heap
[dalvik.vm.heapgrowthlimit]: [128m]
[dalvik.vm.heapmaxfree]: [8m]
[dalvik.vm.heapminfree]: [512k]
[dalvik.vm.heapsize]: [384m]
[dalvik.vm.heapstartsize]: [8m]
[dalvik.vm.heaptargetutilization]: [0.75]
[ro.af.client_heap_size_kbyte]: [4096]
heapgrowthlimit表示单个app可以申请的最大内存空间
heapsize 表示 在AndroidManifest.xml下设置 android:lagerHeap=true时单个app可以申请的最大内存空间
#要占用500M空间，需要两个app服务。
#启动服务 Android 8.0以下通过命令 : am startservice -n com.sun.memoryservicetest/.MemoryServiceTest
#Android 8.0及以上通过命令: am start-foreground-service -n com.sun.memoryservicetest/.MemoryServiceTest
#停止服务通过命令:am stopservice -n com.sun.memoryservicetest/.MemoryServiceTest