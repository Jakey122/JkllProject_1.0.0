
package com.sdk.lib.net;

import org.apache.http.impl.client.DefaultHttpClient;

public interface IDownloadHttpClient {

    IDownloadHttpClient newInstance();

    DefaultHttpClient getDownloadHttpClient();
}
