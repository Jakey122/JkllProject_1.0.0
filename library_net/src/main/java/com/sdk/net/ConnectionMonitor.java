package com.sdk.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * 网络连接状态变化监听器
 */
public class ConnectionMonitor extends BroadcastReceiver {

	private static ArrayList<ConnectionListener> sListtener = new ArrayList<ConnectionListener>();
	public static void registerConnectionMonitor(ConnectionListener listener){
		if(!sListtener.contains(listener)){
			sListtener.add(listener);
		}
	}

	public static void unregisterConnectionMonitor(ConnectionListener listener){
		if(sListtener.contains(listener)){
			sListtener.remove(listener);
		}
	}
			
	private void notification(final int state){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int length = sListtener.size();
				try{
					for(int i=0; i<length; i++){
						sListtener.get(i).connectionStateChanged(state);
					}	
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int state = NetworkStatus.getInstance(context).getNetWorkState();
		notification(state);
	}

}
