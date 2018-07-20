package com.example.admin.memoryleak;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

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
                Intent target = new Intent(Intent.ACTION_VIEW);
                Uri doc = Uri.parse("/app/src/main/res/raw/dantesinferno.pdf");
                File pdftoread = new File("app/src/main/res/raw/dantesinferno.pdf");
                Toast.makeText(MainActivity.this, doc.getPath(), Toast.LENGTH_SHORT).show();
                target.setDataAndType(Uri.fromFile(pdftoread),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }



            }
        });

    }

}
