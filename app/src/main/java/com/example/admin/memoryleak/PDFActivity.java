package com.example.admin.memoryleak;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PDFActivity extends AppCompatActivity {

    private ParcelFileDescriptor fileDescriptor;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ImageView image;
    private Button btnPrevious;
    private Button btnNext;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        // Retain view references.
        image = findViewById(R.id.image);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);

        //set buttons event
        btnPrevious.setOnClickListener(onActionListener(-1)); //previous button clicked
        btnNext.setOnClickListener(onActionListener(1)); //next button clicked

        int index = 0;
        File pdftoread = new File("app/src/main/res/assets/dantesinferno.pdf");
        try {

           // fileDescriptor = ParcelFileDescriptor.dup(getResources().openRawResourceFd(R.raw.dantesinferno).getFileDescriptor());
            fileDescriptor = ParcelFileDescriptor.open(pdftoread, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fileDescriptor);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            index = savedInstanceState.getInt("current_page", 0);
        }
        showPage(index);
    }

    @Override
    public void onDestroy() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer() throws IOException {
        // Reading a PDF file from the assets directory.
        fileDescriptor = getAssets().openFd("dantesinferno.pdf").getParcelFileDescriptor();

        // This is the PdfRenderer we use to render the PDF.
        pdfRenderer = new PdfRenderer(fileDescriptor);
    }

    /**
     * Closes PdfRenderer and related resources.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void closeRenderer() throws IOException {
        if (null != currentPage) {
            currentPage.close();
        }
        pdfRenderer.close();
        fileDescriptor.close();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showPage(int index) {
        if (pdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
        }
        //open a specific page in PDF file
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // showing bitmap to an imageview
        image.setImageBitmap(bitmap);
        updateUIData();
    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("StringFormatInvalid")
    private void updateUIData() {
        int index = currentPage.getIndex();
        int pageCount = pdfRenderer.getPageCount();
        btnPrevious.setEnabled(0 != index);
        btnNext.setEnabled(index + 1 < pageCount);
        setTitle(getString(R.string.app_name, index + 1, pageCount));
    }

    private View.OnClickListener onActionListener(final int i) {
        return new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (i < 0) {//go to previous page
                    showPage(currentPage.getIndex() - 1);
                } else {
                    showPage(currentPage.getIndex() + 1);
                }
            }
        };
    }

}
