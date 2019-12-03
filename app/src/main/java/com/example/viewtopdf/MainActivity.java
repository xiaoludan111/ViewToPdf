package com.example.viewtopdf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mLlTotalView;
    private Button mBtCreatPdf;
    private ScrollView mSc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        initPermission();
        mLlTotalView = (LinearLayout) findViewById(R.id.ll_total_view);
        mBtCreatPdf = (Button) findViewById(R.id.bt_creat_pdf);
        mBtCreatPdf.setOnClickListener(this);
        mSc = (ScrollView) findViewById(R.id.sc);
    }
    public static final int LOGINPRESSION = 0x11;
    public static final int REQUEST_CODE_LOCATION = 0x12;
    public static final int REQUEST_CODE_PRINCIPAL = 0x13;
    public static final int REQUEST_CODE_COPYREASION = 0x14;
    public static final int REQUEST_CODE_USER_SEARCH = 0x15;
    public static final int REQEEST_CDOE_GPS = 0x18;
    private void initPermission() {
        if (PermissionUtil.loginPermission(this, LOGINPRESSION)) {
            if (!PermissionUtil.isOPen(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQEEST_CDOE_GPS);
            }
        }
    }


    @Override
    public void onClick(View view) {
        creatPdf();
    }
    private void creatPdf() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument
                .PageInfo
                .Builder(mSc.getMeasuredWidth(), mSc.getMeasuredHeight(), 1)
                .create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        mSc.draw(page.getCanvas());
        pdfDocument.finishPage(page);

        try {
            String path = Environment.getExternalStorageDirectory() + File.separator + "table.pdf";
            File e = new File(path);
            if (e.exists()) {
                e.delete();
            }
            pdfDocument.writeTo(new FileOutputStream(e));
            Toast.makeText(MainActivity.this, "pdf生成成功!", Toast.LENGTH_SHORT).show();
            Log.e("log","生成的 PDF地址-->"+ e.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }
}
