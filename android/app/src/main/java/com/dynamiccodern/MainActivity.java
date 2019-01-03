package com.dynamiccodern;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.v_1_0_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDynamicCode("1.0.0");
            }
        });

        findViewById(R.id.v_1_0_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDynamicCode("1.0.1");
            }
        });

        findViewById(R.id.v_1_1_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDynamicCode("1.1.0");
            }
        });
    }

    private void runDynamicCode(String version) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("下载中...");
        DownloadTask task = new DownloadTask(new DownloadTask.IDownloadTaskListener() {
            @Override
            public void onPreExecute() {
                progressDialog.show();
            }

            @Override
            public void onPostExecute(String filePath) {
                progressDialog.dismiss();
                if (filePath == null) {
                    Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, DynamicActivity.class);
                    intent.putExtra("filePath", filePath);
                    startActivity(intent);
                }
            }
        });
        task.execute(version);
    }
}
