package com.example.childgamecontrol.plugin;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AppUsageMonitorPlugin extends UniModule {
    private Handler handler;
    private Runnable monitorRunnable;
    private UniJSCallback callback;
    private int timeLimitMinutes;
    
    @UniJSMethod(uiThread = false)
    public void startMonitoring(Map<String, Object> options, UniJSCallback callback) {
        this.callback = callback;
        this.timeLimitMinutes = (int) options.get("timeLimit");
        
        handler = new Handler();
        monitorRunnable = new Runnable() {
            @Override
            public void run() {
                checkAppUsage();
                handler.postDelayed(this, TimeUnit.MINUTES.toMillis(1));
            }
        };
        
        handler.post(monitorRunnable);
    }
    
    @UniJSMethod(uiThread = false)
    public void stopMonitoring() {
        if (handler != null && monitorRunnable != null) {
            handler.removeCallbacks(monitorRunnable);
        }
    }
    
    @UniJSMethod(uiThread = false)
    public void getTodayUsage(UniJSCallback callback) {
        long usage = calculateTodayUsage();
        Map<String, Object> result = new HashMap<>();
        result.put("todayUsage", TimeUnit.MILLISECONDS.toMinutes(usage));
        callback.invoke(result);
    }
    
    @UniJSMethod(uiThread = true)
    public void selectGames(UniJSCallback callback) {
        Activity activity = mUniSDKInstance.getContext();
        Intent intent = new Intent(activity, GameSelectionActivity.class);
        activity.startActivity(intent);
        
        // 这里简化处理，实际需要从Activity返回结果
        callback.invoke(new HashMap<String, Object>());
    }
    
    private void checkAppUsage() {
        long todayUsage = calculateTodayUsage();
        if (TimeUnit.MILLISECONDS.toMinutes(todayUsage) >= timeLimitMinutes) {
            Map<String, Object> result = new HashMap<>();
            result.put("event", "TIME_LIMIT_EXCEEDED");
            callback.invoke(result);
        }
    }
    
    private long calculateTodayUsage() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) mUniSDKInstance.getContext()
                .getSystemService(Context.USAGE_STATS_SERVICE);
        
        long endTime = System.currentTimeMillis();
        long startTime = endTime - TimeUnit.DAYS.toMillis(1);
        
        List<UsageStats> stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        
        long totalUsage = 0;
        List<String> gamePackages = getGamePackages();
        
        for (UsageStats usageStats : stats) {
            if (gamePackages.contains(usageStats.getPackageName())) {
                totalUsage += usageStats.getTotalTimeInForeground();
            }
        }
        
        return totalUsage;
    }
    
    private List<String> getGamePackages() {
        // 从存储中获取选中的游戏包名
        // 实际实现需要从SharedPreferences或数据库读取
        return Arrays.asList("com.tencent.ig", "com.riotgames.league.wildrift");
    }
}