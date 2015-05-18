package reception;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import model.SampleManager;

/**
 * Created by erevear on 5/16/15.
 */
public class BatteryReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryLevelReceiver";
    String connection;

    public BatteryReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName receiver = new ComponentName(context, UploadService.class);
        PackageManager pm = context.getPackageManager();
        Log.i(TAG, "onReceive" + intent.getAction().toString());
        connection = intent.getAction().toString();

        if(connection.equals("android.intent.action.ACTION_POWER_CONNECTED")){
            Log.d(TAG, "connection is true");
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            SampleManager.startIntent(context, false);
            SampleManager.changePolling(60000);
            SampleManager.startIntent(context, true);
           // userTime();

        } else if (connection.equals("android.intent.action.ACTION_POWER_DISCONNECTED")){
            Log.d(TAG, "connection is false");
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            SampleManager.startIntent(context, false);
            SampleManager.changePolling(300000);
            SampleManager.startIntent(context, true);
            //savePowerTime();

        }
    }

    public void userTime() {


    }

    public void savePowerTime() {
        Log.d(TAG, "save power time");
        SampleManager.changePolling(300000);

    }
}