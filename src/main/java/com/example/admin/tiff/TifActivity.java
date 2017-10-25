package com.example.admin.tiff;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.FilePicker;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;


public class TifActivity extends AppCompatActivity implements FilePicker.FilePickerSupport{
    public  static final String INPUT_FILE_PATH="input_file_path";
    private String filePath;
    private RelativeLayout rl_Container;
    private MuPDFCore core;
    private MuPDFReaderView mDocView;
    private MuPDFPageAdapter adapter;
    private Context mContext;
    private TextView tv_Back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tif);
        mContext=this;
        rl_Container=(RelativeLayout) findViewById(R.id.rl_Container);
        tv_Back=(TextView) findViewById(R.id.tv_Back);
        tv_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            filePath = extras.getString(INPUT_FILE_PATH);
            loadTiff(filePath);
        }
    }


    private void loadTiff(String path){
        core = openFile(path);
        if (core == null){
            return;
        }
        mDocView = new MuPDFReaderView(mContext);
        adapter=new MuPDFPageAdapter(mContext,this, core);
        mDocView.setAdapter(adapter);
        mDocView.setDisplayedViewIndex(0);
        rl_Container.addView(mDocView);
    }

    private MuPDFCore openFile(String path){
        try
        {
            core = new MuPDFCore(mContext,path);
            // New file: drop the old outline data
        }
        catch (Exception e)
        {
            Log.i("TAG","e---="+e.toString());
            return null;
        }
        return core;
    }

    @Override
    public void performPickFor(FilePicker picker) {
    }

}
