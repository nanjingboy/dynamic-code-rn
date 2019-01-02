package com.dynamiccodern;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
        Intent intent = new Intent(this, DynamicActivity.class);
        startActivity(intent);
    }
}
