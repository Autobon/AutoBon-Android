package cn.com.incardata.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.com.incardata.service.AutobonService;

public class RestartReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent intentReceiver = new Intent(context, AutobonService.class);
		context.startService(intentReceiver);
		Log.d("RestartReceiver", "startService");
		return;
	}

	private boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("cn.com.incardata.service.AutobonService".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
