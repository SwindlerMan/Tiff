package com.example.admin.tiff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_Open=(Button) findViewById(R.id.bt_Open);
        bt_Open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,TifActivity.class);
                //intent.putExtra(TifActivity.INPUT_FILE_PATH,"Tif文件路径");
                intent.putExtra(TifActivity.INPUT_FILE_PATH,"/storage/emulated/0/Android/03.tif");
                startActivity(intent);
            }
        });
    }
}



