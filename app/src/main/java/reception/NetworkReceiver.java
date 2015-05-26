package reception;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by erevear on 5/16/15.
 */
public class NetworkReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NETWORKRECEIVER", "network intent received");
//        ComponentName receiver = new ComponentName(context, SampleService.class);
//        PackageManager pm = context.getPackageManager();


        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        boolean isConnected = info != null &&
                info.isConnectedOrConnecting();

            if (isConnected == true) {
                //start service
                Log.d("NETWORKRECEIVER", "is connected " + info.isConnected());
//                Intent start = new Intent(context, SampleService.class);
//                context.stopService(start);
//                pm.setComponentEnabledSetting(receiver,
//                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                        PackageManager.DONT_KILL_APP);

                //UploadService.setServiceAlarm(context, true);
            }else {
                //stop service
                Log.d("NETWORKRECEIVER", "is connected false");
//                pm.setComponentEnabledSetting(receiver,
//                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                        PackageManager.DONT_KILL_APP);
//                Intent stop = new Intent(context, SampleService.class);
//                context.stopService(stop);
                //UploadService.setServiceAlarm(context, false);
            }

    }
}