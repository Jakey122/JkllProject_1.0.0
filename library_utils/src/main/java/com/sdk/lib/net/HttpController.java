
package com.sdk.lib.net;

import android.content.Context;
import android.text.TextUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * used to network communicate
 */
public abstract class HttpController {
	
	private MyHttpClient mHttpClient;
	private ConcurrentHashMap<String, Future<?>> mTasks = new ConcurrentHashMap<String, Future<?>>();
	private ExecutorService mThreadPool;
	public Context mContext;
	
	public HttpController() {
		
	}
	
	public HttpController(Context sContext) {
		this.mContext = sContext.getApplicationContext();
		mHttpClient = new MyHttpClient(mContext.getApplicationContext());
		mThreadPool = Executors.newFixedThreadPool(1);
	}

    public void execute(Runnable runnable) {
		execute(null, runnable);
	}

	private void execute(String key, Runnable runnable) {
		try {
			if (mThreadPool != null) {
				Future<?> sFuture = mThreadPool.submit(runnable);

				if (!TextUtils.isEmpty(key)) {
					if (mTasks.contains(key)) {
						Future<?> tempFuture = mTasks.get(key);
						tempFuture.cancel(true);
					}
					mTasks.put(key, sFuture);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String doPost(String url, HttpEntity entity) throws ClientProtocolException, IOException{
		return mHttpClient.doPost(url, entity);
	}
	
	public String doGet(String url) throws ClientProtocolException, IOException{
		return mHttpClient.doGet(url);
	}
	
	public MyHttpClient getHttpClient(){
		if(mHttpClient == null)
			mHttpClient = new MyHttpClient(mContext.getApplicationContext());
		return mHttpClient;
	}
}
