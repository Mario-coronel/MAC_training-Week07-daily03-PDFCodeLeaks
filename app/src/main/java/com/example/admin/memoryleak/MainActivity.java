package com.example.admin.memoryleak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static TextView tvresult;
    private Button btnScanCode,btnRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvresult = findViewById(R.id.tvresult);

        btnScanCode = findViewById(R.id.scanCode);

        btnScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                    startActivity(intent);
            }
        });

        btnRender = findViewById(R.id.renderPDF);
        btnRender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PDFActivity.class);
                startActivity(intent);
            }
        });

    }

}
