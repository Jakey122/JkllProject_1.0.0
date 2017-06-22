
package com.sdk.net;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public abstract class HttpDownloaderImp {

    protected static String TAG = "DownloadTask";
    protected static final int MAX_RETRY = 3;
    protected static final int DEFAULT_CHUNK_SIZE = 4096;
    protected static final int DEFAULT_REFRESH_RATE = 500;

    protected DownloadListener mListener = null;
    protected Context mContext;
    protected String mDownloadUrl;
    protected long totalSize;
    protected long prevTime = 0;

    protected boolean mCancel = false;
    protected boolean mRequest = false;

    public HttpDownloaderImp() {

    }

    public HttpDownloaderImp(Context context, String downloadUrl,
            DownloadListener listener) {
        mContext = context;
        mListener = listener;
        mDownloadUrl = downloadUrl;
    }

    public abstract HttpDownloaderImp newInstance(Context context, String downloadUrl,
            DownloadListener listener);

    public long download(OutputStream os) throws Exception {
        boolean retry;
        int i = 0;
        long downloadedSize = 0;
        do {
            ++i;
            retry = false;
            try {
                if (downloadedSize == 0) {
                    downloadedSize = download(os, -1);
                } else {
                    Log.d(TAG, "Resuming download at " + downloadedSize);
                    downloadedSize = download(os, downloadedSize);
                }
            } catch (Exception de) {
                if (i < MAX_RETRY) {
                    retry = true;
                    downloadedSize = downloadedSize;
                } else {
                    throw de;
                }
            }
        } while (retry);
        return downloadedSize;
    }

    public long download(OutputStream os, long startOffset) throws Exception {
        // TODO Auto-generated method stub
        mCancel = false;
        long downloadedSize = startOffset > 0 ? startOffset : 0;
        InputStream is = null;
        boolean errorWritingStream = false;
        boolean resume = false;

        try {
            if (startOffset > 0) {
                // This is a breakpoint download setting. Add the request proper
                // mHttpRequest.setHeader("Range", "bytes=" + startOffset + "-");
                setRequestHeader(startOffset);
                resume = true;
            }
            mRequest = true;
            // HttpResponse httpResponse = mHttpApi.executeHttpRequest(mHttpRequest);
            // int respCode = httpResponse.getStatusLine().getStatusCode();
            int respCode = getRespCode();
            Log.d(TAG, "Response is: " + respCode);
            // HttpEntity entity = httpResponse.getEntity();
            HttpEntity entity = getHttpEntity();
            totalSize = entity.getContentLength() + (startOffset > 0 ? startOffset : 0);

            if (mListener != null) {
                mListener.downloadStarted(mDownloadUrl, totalSize);
            }
            boolean ok;
            if (resume) {
                if (respCode == HttpURLConnection.HTTP_PARTIAL) {
                    ok = true;
                } else if (respCode == 416) {
                    return startOffset;
                } else {
                    Log.d(TAG, "Server refused resuming download");
                    throw new Exception("Cannot resume download");
                }
            } else {
                ok = respCode == HttpURLConnection.HTTP_OK;
            }
            if (ok) {
                is = new InputStreamWrapper(entity.getContent(), entity);
                byte[] data = new byte[DEFAULT_CHUNK_SIZE];
                int n = 0;
                long lastListened = 0;
                prevTime = System.currentTimeMillis();

                while ((n = is.read(data)) != -1 && !isDownloadCancelled()) {
                    // 下载了多少字节 保存在本地
                    downloadedSize += n;
                    long now = System.currentTimeMillis();
                    try {
                        os.write(data, 0, n);
                    } catch (IOException ioe) {
                        Log.e(TAG, "Cannot write output stream", ioe);
                        errorWritingStream = true;
                        break;
                    }
                    if (mListener != null && now - lastListened > DEFAULT_REFRESH_RATE) {
                        lastListened = now;
                        mListener.downloadProgress(mDownloadUrl, downloadedSize, totalSize);
                    }
                }
            } else {
                throw new Exception("HTTP error code: " + respCode);
            }
        } catch (IOException ex) {
            if (isDownloadCancelled()) {
                throw new Exception(downloadedSize+"");
            } else {
                throw new Exception(downloadedSize+"");
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                os = null;
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
        }
        if (isDownloadCancelled()) {
            mRequest = false;
            mCancel = false;
            throw new Exception(downloadedSize+"");
        }
        if (errorWritingStream) {
            mRequest = false;
            mCancel = false;
            throw new Exception(downloadedSize+"");
        }
        mRequest = false;
        mCancel = false;
        return downloadedSize;
    }

    public void setDownloadListener(DownloadListener listener) {
        mListener = listener;
    }

    public long resume(OutputStream os, long startOffset) throws Exception {
        return download(os, startOffset);
    }

    protected boolean isDownloadCancelled() {
        return mCancel;
    }

    protected boolean isDownloading() {
        return mRequest;
    }

    public abstract void cancel();

    protected abstract int getRespCode();

    protected abstract void setRequestHeader(long startOffset);

    protected abstract HttpEntity getHttpEntity();

    public long getTotalSize() {
        return totalSize;
    }

    protected class InputStreamWrapper extends InputStream {
        private InputStream is;
        private HttpEntity entity;

        public InputStreamWrapper(InputStream is, HttpEntity entity) {
            this.is = is;
            this.entity = entity;
        }

        public int available() throws IOException {
            return is.available();
        }

        public void close() throws IOException {
            is.close();
            entity.consumeContent();
        }

        public void mark(int limit) {
            is.mark(limit);
        }

        public boolean markSupported() {
            return is.markSupported();
        }

        public int read() throws IOException {
            return is.read();
        }

        public int read(byte buf[]) throws IOException {
            return is.read(buf);
        }

        public int read(byte buf[], int off, int len) throws IOException {
            return is.read(buf, off, len);
        }

        public void reset() throws IOException {
            is.reset();
        }

        public long skip(long n) throws IOException {
            return is.skip(n);
        }
    }

    public interface DownloadListener {

        public void downloadStarted(String downloadUrl, long totalSize);

        public void downloadProgress(String downloadUrl, long progressSize, long totalSize);

        public void downloadEnded(String downloadUrl);
    }
}
