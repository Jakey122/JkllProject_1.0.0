
package com.sdk.lib.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sdk.lib.util.Util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MyHttpClient {

    private static final String TAG = "MyHttpClient";
    // private static final String CLIENT_VERSION_HEADER = "User-Agent";
    private static final int TIMEOUT = 25 * 1000;// 25 Seconds
    private static final int CONNECTIONTIMEOUT = 12 * 1000;// 12 Seconds
    private DefaultHttpClient mHttpClient;
    private Context mContext;
    
    private static IDownloadHttpClient mClient;

    public static void initIDownloadHttpClient(IDownloadHttpClient client) {
        mClient = client;
    }
    
    public MyHttpClient(Context context) {
        mContext = context.getApplicationContext();
        mHttpClient = createHttpClient();
    }

    public String doGet(String url) throws ClientProtocolException, IOException {
        Log.w(TAG, "MyHttpClient doGet url = " + url);

        if (!NetworkStatus.getInstance(mContext).isConnected()) {
            return null;
        }

        HttpGet sHttpGet = createHttpGet(url);
        HttpResponse sHttpResponse = executeHttpRequest(sHttpGet);
        String strResponse = getResponseData(sHttpResponse);

        // String fileName = url.substring(url.indexOf("method") + 7, url.indexOf("method") + 8) +
        // ".txt";
        // File sFile = new File(App.getInstance().getCacheDir() + File.separator + fileName);
        //
        // if (!sFile.exists() || sFile.length() == 0) {
        // FileOutputStream os = new FileOutputStream(sFile);
        // os.write(strResponse.getBytes());
        // os.flush();
        // os.close();
        // }

        return strResponse;
    }

    public String doPost(String url) throws ClientProtocolException, IOException {
        Log.w(TAG, "MyHttpClient doPost url = " + url);
        if (!NetworkStatus.getInstance(mContext).isConnected()) {
            return null;
        }
        HttpPost sHttpPost = createHttpPost(url);
        return doPost(sHttpPost);
    }

    public String doPost(String url, HttpEntity entity) throws ClientProtocolException, IOException {
        Log.w(TAG, "MyHttpClient doPost url = " + url);
        if (!NetworkStatus.getInstance(mContext).isConnected()) {
            return null;
        }
        HttpPost sHttpPost = createHttpPost(url);
        sHttpPost.setEntity(entity);
        return doPost(sHttpPost);
    }

    public String doPost(HttpPost httpPost) throws ClientProtocolException, IOException {
        if (!NetworkStatus.getInstance(mContext).isConnected()) {
            return null;
        }

        HttpResponse sHttpResponse = executeHttpRequest(httpPost);
        String strResponse = getResponseData(sHttpResponse);
        return strResponse;
    }

    private String getResponseData(HttpResponse httpResponse) throws IOException {
        String strResponse = null;

        if (httpResponse != null) {
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity sHttpEntity = httpResponse.getEntity();

            if (responseCode == HttpURLConnection.HTTP_OK && sHttpEntity != null) {
                Header sHeader = sHttpEntity.getContentEncoding();

                if (sHeader != null && sHeader.getValue().contains("gzip")) {
                    byte[] data = Util.gzipDecompress(sHttpEntity.getContent());
                    strResponse = new String(data, "utf-8");
                } else {
                    strResponse = EntityUtils.toString(sHttpEntity, "utf-8");
                }
            }
        }

        if (!TextUtils.isEmpty(strResponse)) {
            strResponse = strResponse.trim();
        }
        return strResponse;
    }

    @SuppressWarnings("unused")
    private HttpResponse doHttpPost(String url, byte[] params) throws ClientProtocolException,
            IOException {
        HttpPost sHttpPost = createHttpPost(url, params);
        return executeHttpRequest(sHttpPost);
    }

    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest)
            throws ClientProtocolException, IOException {
        try {
            HttpResponse sHttpResponse = mHttpClient.execute(httpRequest);
            Log.i(TAG, "http response code-->" + sHttpResponse.getStatusLine().getStatusCode());
            return sHttpResponse;
        } catch (UnknownHostException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    public HttpPost createHttpPost(String url) {
        HttpPost sHttpPost = new HttpPost(url);
        return sHttpPost;
    }

    @SuppressWarnings("unused")
    private HttpPost createHttpPost(String url, String params) throws UnsupportedEncodingException {
        HttpPost sHttpPost = createHttpPost(url);
        sHttpPost.setEntity(new StringEntity(params, HTTP.UTF_8));
        return sHttpPost;
    }

    private HttpPost createHttpPost(String url, byte[] params) throws UnsupportedEncodingException {
        HttpPost sHttpPost = createHttpPost(url);
        sHttpPost.setEntity(new ByteArrayEntity(params));
        return sHttpPost;
    }

    @SuppressWarnings("unused")
    private HttpPost createHttpPost(String url, NameValuePair... pairs)
            throws UnsupportedEncodingException {
        HttpPost sHttpPost = createHttpPost(url);
        sHttpPost.setEntity(new UrlEncodedFormEntity(formatParams(pairs), HTTP.UTF_8));
        return sHttpPost;
    }

    public HttpGet createHttpGet(String url) {
        HttpGet sHttpGet = new HttpGet(url);
        return sHttpGet;
    }

    @SuppressWarnings("unused")
    private HttpGet createHttpGet(String url, NameValuePair... pairs) {
        String params = URLEncodedUtils.format(formatParams(pairs), HTTP.UTF_8);
        return createHttpGet(url + "?" + params);
    }

    private ArrayList<NameValuePair> formatParams(NameValuePair... pairs) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        if (pairs != null) {
            for (NameValuePair nameValuePair : pairs) {
                if (nameValuePair != null) {
                    params.add(nameValuePair);
                }
            }
        }
        return params;
    }

    public static final DefaultHttpClient createHttpClient() {
        if(mClient != null) {
            return mClient.newInstance().getDownloadHttpClient();
        }
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        supportedSchemes.register(new Scheme("https", new EasySSLSocketFactory(), 443));

        final HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, true);

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
                supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private static final HttpParams createHttpParams() {
        final HttpParams params = new BasicHttpParams();

        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, CONNECTIONTIMEOUT);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        return params;
    }

    /**
     * This socket factory will create ssl socket that accepts everything
     */
    public static class EasySSLSocketFactory implements SocketFactory, LayeredSocketFactory {

        private static SocketFactory instance = new EasySSLSocketFactory();

        private SSLContext sslcontext = null;

        private static SSLContext createEasySSLContext() throws IOException {
            try {
                SSLContext context = SSLContext.getInstance("TLS");

                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {

                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            // do nothing
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            // do nothing
                        }

                    }
                };

                context.init(null, trustAllCerts, new SecureRandom());

                return context;
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }

        private SSLContext getSSLContext() throws IOException {
            if (this.sslcontext == null) {
                this.sslcontext = createEasySSLContext();
            }
            return this.sslcontext;
        }

        /**
         * @see SocketFactory#connectSocket(Socket,
         *      String, int, InetAddress, int, HttpParams)
         */
        public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress,
                int localPort,
                HttpParams params) throws IOException, UnknownHostException,
                ConnectTimeoutException {
            int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
            int soTimeout = HttpConnectionParams.getSoTimeout(params);

            InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
            SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

            if ((localAddress != null) || (localPort > 0)) {
                // we need to bind explicitly
                if (localPort < 0) {
                    localPort = 0; // indicates "any"
                }
                InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
                sslsock.bind(isa);
            }

            sslsock.connect(remoteAddress, connTimeout);
            sslsock.setSoTimeout(soTimeout);
            return sslsock;

        }

        /**
         * @see SocketFactory#createSocket()
         */
        public Socket createSocket() throws IOException {
            return getSSLContext().getSocketFactory().createSocket();
        }

        /**
         * @see SocketFactory#isSecure(Socket)
         */
        public boolean isSecure(Socket socket) throws IllegalArgumentException {
            return true;
        }

        /**
         * @see LayeredSocketFactory#createSocket(Socket,
         *      String, int, boolean)
         */
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException,
                UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        public boolean equals(Object obj) {
            return ((obj != null) && obj.getClass().equals(EasySSLSocketFactory.class));
        }

        public int hashCode() {
            return EasySSLSocketFactory.class.hashCode();
        }

        public static SocketFactory getSocketFactory() {
            return instance;
        }

    }

}
