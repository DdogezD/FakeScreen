package me.neversleep.plusplus;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.N)
public class QuickStartService extends TileService {
    public static final String TAG = "QuickStartService";
    private SharedPreferences xConf;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            this.xConf = getSharedPreferences("x_conf", 1);
            Log.e(TAG, "onCreate: xConf" + this.xConf);
        } catch (SecurityException e) {
            Toast.makeText(this, "error: " + e.getMessage(), 1).show();
        }
    }

    @Override
    public void onClick() {
        super.onClick();
        if (this.xConf == null) {
            return;
        }
        int state = getQsTile().getState();
        if (state == 1) {
            getQsTile().setState(2);
            getQsTile().setLabel("仅熄屏");
            this.xConf.edit().putBoolean("power", true).apply();
        } else if (state == 2) {
            getQsTile().setState(1);
            getQsTile().setLabel("正常休眠");
            this.xConf.edit().putBoolean("power", false).apply();
        }
        getQsTile().updateTile();
        Log.e(TAG, "onClick: " + getQsTile().getState());
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        if (this.xConf == null) {
            return;
        }
        boolean isPowerOn = this.xConf.getBoolean("power", false);
        getQsTile().setState(isPowerOn ? 2 : 1);
        getQsTile().setLabel(isPowerOn ? "仅熄屏" : "正常休眠");
        getQsTile().updateTile();
        Log.e(TAG, "onTileAdded: " + isPowerOn + ":" + getQsTile().getState());
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        if (this.xConf == null) {
            return;
        }
        boolean isPowerOn = this.xConf.getBoolean("power", false);
        getQsTile().setState(isPowerOn ? 2 : 1);
        getQsTile().setLabel(isPowerOn ? "仅熄屏" : "正常休眠");
        getQsTile().updateTile();  // 强制更新 tile 状态
        Log.e(TAG, "onStartListening: update" + isPowerOn + ":" + getQsTile().getState());
    }
}
