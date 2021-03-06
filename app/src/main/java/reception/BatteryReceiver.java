package reception;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

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
        //ComponentName receiver = new ComponentName(context, UploadService.class);
        PackageManager pm = context.getPackageManager();
        Log.i(TAG, "onReceive" + intent.getAction().toString());
        connection = intent.getAction().toString();

        if(connection.equals("android.intent.action.ACTION_POWER_CONNECTED")){
            Log.d(TAG, "connection is true");
//            pm.setComponentEnabledSetting(receiver,
//                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                    PackageManager.DONT_KILL_APP);



        } else if (connection.equals("android.intent.action.ACTION_POWER_DISCONNECTED")){
            Log.d(TAG, "connection is false");
//            pm.setComponentEnabledSetting(receiver,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP);

        }
    }
}