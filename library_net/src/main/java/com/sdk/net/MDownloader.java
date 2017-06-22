package com.sdk.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.sdk.util.PackageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;

/**
 * Created by root on 16-8-19.
 */
public class MDownloader extends HttpDownloaderImp {

    //限速标志(单位:Kb/s)
    private static final double LimitSpeed = 300;

    private HttpClient mClient;
    private String mDownloadUrl;
    private HttpUriRequest mHttpRequest;
    private HttpResponse mHttpResponse;

    public MDownloader() {
        super();
    }

    public MDownloader(Context context, String downloadUrl, HttpDownloaderImp.DownloadListener listener) {
        super(context, downloadUrl, listener);
        mDownloadUrl = downloadUrl;
//        mClient = AsyncHttpClientHelper.getHttpClient();
//        mClient = new SyncHttpClient().getHttpClient();
        mClient = new AsyncHttpClient().getHttpClient();
        mHttpRequest = new HttpGet(downloadUrl);
    }

    @Override
    public HttpDownloaderImp newInstance(Context context, String s, DownloadListener downloadListener) {
        return new MDownloader(context, s, downloadListener);
    }

    @Override
    public void cancel() {
        mCancel = true;
        if (mRequest) {
            mHttpRequest.abort();
        }
    }

    @Override
    protected int getRespCode() {
        int respCode = 0;
        try {
            mHttpResponse = mClient.execute(mHttpRequest);
            respCode = mHttpResponse.getStatusLine().getStatusCode();
        } catch (Exception e) {
        }
        return respCode;
    }

    @Override
    protected void setRequestHeader(long startOffset) {
        mHttpRequest.setHeader("Range", "bytes=" + startOffset + "-");
    }

    @Override
    protected org.apache.http.HttpEntity getHttpEntity() {
        if (mHttpResponse == null) {
            try {
                mHttpResponse = mClient.execute(mHttpRequest);
            } catch (Exception e) {
            }
        }
        if (mHttpResponse != null)
            return new MHttpEntity(mHttpResponse.getEntity());
        return null;
    }

    class MHttpEntity implements org.apache.http.HttpEntity {
        HttpEntity mHttpEntity;

        public MHttpEntity(HttpEntity httpEntity) {
            mHttpEntity = httpEntity;
        }

        @Override
        public boolean isRepeatable() {
            if (mHttpEntity != null) return mHttpEntity.isRepeatable();
            return false;
        }

        @Override
        public boolean isChunked() {
            if (mHttpEntity != null) return mHttpEntity.isChunked();
            return false;
        }

        @Override
        public long getContentLength() {
            if (mHttpEntity != null) return mHttpEntity.getContentLength();
            return 0;
        }

        @Override
        public org.apache.http.Header getContentType() {
            if (mHttpEntity != null) return new MHeader(mHttpEntity.getContentType());
            return null;
        }

        @Override
        public org.apache.http.Header getContentEncoding() {
            if (mHttpEntity != null) return new MHeader(mHttpEntity.getContentEncoding());
            return null;
        }

        @Override
        public InputStream getContent() throws IOException, IllegalStateException {
            if (mHttpEntity != null) return mHttpEntity.getContent();
            return null;
        }

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
            if (mHttpEntity != null) mHttpEntity.writeTo(outputStream);
        }

        @Override
        public boolean isStreaming() {
            if (mHttpEntity != null) return mHttpEntity.isStreaming();
            return false;
        }

        @Override
        public void consumeContent() throws IOException {
            if (mHttpEntity != null) mHttpEntity.consumeContent();
        }
    }

    class MHeader implements org.apache.http.Header {

        Header mHeader;

        public MHeader(Header header) {
            mHeader = header;
        }

        @Override
        public String getName() {
            if (mHeader != null) return mHeader.getName();
            return null;
        }

        @Override
        public String getValue() {
            if (mHeader != null) return mHeader.getValue();
            return null;
        }

        @Override
        public org.apache.http.HeaderElement[] getElements() throws org.apache.http.ParseException {
            if (mHeader != null) {
                HeaderElement[] rawElements = mHeader.getElements();
                if (rawElements != null && rawElements.length > 0) {
                    org.apache.http.HeaderElement[] mHeaderElements = new org.apache.http.HeaderElement[rawElements.length];
                    for (int i = 0; i < rawElements.length; i++) {
                        HeaderElement element = rawElements[i];
                        mHeaderElements[i] = new MHeaderElement(element);
                    }
                    return mHeaderElements;
                }
            }
            return new org.apache.http.HeaderElement[0];
        }
    }

    class MHeaderElement implements org.apache.http.HeaderElement {

        HeaderElement mHeaderElement;

        public MHeaderElement(HeaderElement headerElement) {
            mHeaderElement = headerElement;
        }

        @Override
        public String getName() {
            if (mHeaderElement != null) return mHeaderElement.getName();
            return null;
        }

        @Override
        public String getValue() {
            if (mHeaderElement != null) return mHeaderElement.getValue();
            return null;
        }

        @Override
        public org.apache.http.NameValuePair[] getParameters() {
            if (mHeaderElement != null) {
                NameValuePair[] rawNameValuePair = mHeaderElement.getParameters();
                if (rawNameValuePair != null && rawNameValuePair.length > 0) {
                    org.apache.http.NameValuePair[] mNameValuePairs = new org.apache.http.NameValuePair[rawNameValuePair.length];
                    for (int i = 0; i < rawNameValuePair.length; i++) {
                        NameValuePair pair = rawNameValuePair[i];
                        mNameValuePairs[i] = new MNameValuePair(pair);
                    }
                    return mNameValuePairs;
                }
            }
            return new org.apache.http.NameValuePair[0];
        }

        @Override
        public org.apache.http.NameValuePair getParameterByName(String s) {
            if (mHeaderElement != null)
                return new MNameValuePair(mHeaderElement.getParameterByName(s));
            return null;
        }

        @Override
        public org.apache.http.NameValuePair getParameter(int i) {
            if (mHeaderElement != null)
                return new MNameValuePair(mHeaderElement.getParameter(i));
            return null;
        }

        @Override
        public int getParameterCount() {
            if (mHeaderElement != null) return mHeaderElement.getParameterCount();
            return 0;
        }

    }

    class MNameValuePair implements org.apache.http.NameValuePair {
        NameValuePair mNameValuePair;

        public MNameValuePair(NameValuePair nameValuePair) {
            mNameValuePair = nameValuePair;
        }

        @Override
        public String getName() {
            if (mNameValuePair != null) return mNameValuePair.getName();
            return null;
        }

        @Override
        public String getValue() {
            if (mNameValuePair != null) return mNameValuePair.getValue();
            return null;
        }
    }

}
