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

import ir.mahdi.mzip.zip.ZipArchive;

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
            URL url = new URL(String.format("http://10.0.3.2:8080/%s/android.zip", version));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int code = connection.getResponseCode();
            if (code != 200) {
                return null;
            }
            String basePath = getDistFilePath(version);
            File file = new File(basePath, "android.zip");
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
            ZipArchive.unzip(file.getAbsolutePath(), String.format("%s/android", basePath), "");
            remove(file);
            return String.format("%s/android/index.bundle", basePath);
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
        File file = new File(path);
        remove(file);
        file.mkdirs();
        return path;
    }

    private void remove(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (File child: file.listFiles()) {
                if (child.isDirectory()) {
                    remove(child);
                } else {
                    child.delete();
                }
            }
        }
        file.delete();
    }
}
