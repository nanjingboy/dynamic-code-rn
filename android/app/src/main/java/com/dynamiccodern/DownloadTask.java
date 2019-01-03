package com.dynamiccodern;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Integer, String> {

    private IDownloadTaskListener mListener;

    public interface IDownloadTaskListener {
        void onPreExecute();
        void onPostExecute(String filePath);
    }

    public DownloadTask(IDownloadTaskListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        if (mListener != null) {
            mListener.onPreExecute();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String version = params[0];
            URL url = new URL(String.format("http://10.0.27.63:8080/%s/index.android.bundle", version));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if (code != 200) {
                return null;
            }
            File file = new File(getDistFilePath(version), "index.android.bundle");
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
            return file.getAbsolutePath();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String filePath) {
        if (mListener != null) {
            mListener.onPostExecute(filePath);
        }
    }

    private String getDistFilePath(String version) {
        String path = String.format(
                "%s/%s/%s",
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                BuildConfig.APPLICATION_ID, version
        );
        (new File(path)).mkdirs();
        return path;
    }
}
