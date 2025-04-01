package com.example.childgamecontrol.plugin;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GameSelectionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        
        List<String> gameNames = new ArrayList<>();
        for (ApplicationInfo packageInfo : packages) {
            // 简单过滤游戏应用（实际需要更精确的判断）
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                gameNames.add(pm.getApplicationLabel(packageInfo).toString());
            }
        }
        
        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_multiple_choice, gameNames);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        setContentView(listView);
    }
}